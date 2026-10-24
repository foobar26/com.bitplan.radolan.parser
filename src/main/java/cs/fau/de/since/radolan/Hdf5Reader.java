package cs.fau.de.since.radolan;

import cs.fau.de.since.radolan.Catalog.Unit;
import io.jhdf.HdfFile;
import io.jhdf.api.Attribute;
import io.jhdf.api.Dataset;
import io.jhdf.api.Node;

import java.io.File;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.lang.reflect.Array;

/**
 * Reads the HDF5 (ODIM_H5) composite format which DWD uses for the radar
 * composites (e.g. composite_wn_*-hd5) since 2024. The grid is stored as raw
 * integers which are converted to the physical value (dBZ) by
 * <code>value = raw * gain + offset</code>.
 */
public class Hdf5Reader {
  private static final byte[] MAGIC = { (byte) 0x89, 'H', 'D', 'F', '\r', '\n',
      0x1a, '\n' };
  private static final String DATA_PATH = "dataset1/data1/data";
  private static final String DATA_WHAT_PATH = "dataset1/data1/what";
  private static final String DATASET_WHAT_PATH = "dataset1/what";
  private static final String WHAT_PATH = "what";
  private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter
      .ofPattern("yyyyMMddHHmmss");

  /**
   * value used for undetected / no data cells - same as the lower end of the
   * classic RADOLAN dBZ products
   */
  public static final float NO_DATA = -32.5F;

  /**
   * @return true if the given bytes start with the HDF5 signature
   */
  public static boolean isHdf5(byte[] bytes) {
    if (bytes == null || bytes.length < MAGIC.length) {
      return false;
    }
    for (int i = 0; i < MAGIC.length; i++) {
      if (bytes[i] != MAGIC[i]) {
        return false;
      }
    }
    return true;
  }

  /**
   * fill the given composite from the HDF5 bytes
   *
   * @param c
   *          - the composite to fill
   * @param bytes
   *          - the content of the HDF5 file
   * @throws Exception
   */
  public static void read(Composite c, byte[] bytes) throws Exception {
    // jhdf needs a file
    File tmp = File.createTempFile("radolan", ".h5");
    try {
      Files.write(tmp.toPath(), bytes);
      try (HdfFile hdf = new HdfFile(tmp)) {
        readHdf(c, hdf);
      }
    } finally {
      tmp.delete();
    }
  }

  private static void readHdf(Composite c, HdfFile hdf) throws Exception {
    Node dataNode = hdf.getByPath(DATA_PATH);
    if (!(dataNode instanceof Dataset)) {
      throw new Exception("HDF5 composite without " + DATA_PATH);
    }
    Node what = hdf.getByPath(DATA_WHAT_PATH);
    double gain = getDouble(what, "gain", 1.0);
    double offset = getDouble(what, "offset", 0.0);
    double nodata = getDouble(what, "nodata", Double.NaN);
    double undetect = getDouble(what, "undetect", Double.NaN);

    Object data = ((Dataset) dataNode).getData();
    int[] dims = ((Dataset) dataNode).getDimensions();
    if (dims.length != 2) {
      throw new Exception("unexpected HDF5 data dimensions " + dims.length);
    }
    int py = dims[0];
    int px = dims[1];
    float[][] plain = new float[py][px];
    for (int y = 0; y < py; y++) {
      Object row = Array.get(data, y);
      for (int x = 0; x < px; x++) {
        double raw = unsigned(Array.get(row, x));
        if (raw == nodata || raw == undetect) {
          plain[y][x] = NO_DATA;
        } else {
          plain[y][x] = (float) (raw * gain + offset);
        }
      }
    }
    // the file is stored top row first as the classic format
    c.PlainData = plain;
    c.setPx(px);
    c.setPy(py);
    c.setDx(px);
    c.setDy(py);
    c.setProduct("WN");
    c.setDataUnit(Unit.Unit_unknown);
    c.setPrecision(0);

    Node whatRoot = hdf.getByPath(WHAT_PATH);
    ZonedDateTime capture = parseTime(getString(whatRoot, "date"),
        getString(whatRoot, "time"));
    Node dsWhat = hdf.getByPath(DATASET_WHAT_PATH);
    ZonedDateTime start = parseTime(getString(dsWhat, "startdate"),
        getString(dsWhat, "starttime"));
    c.CaptureTime = capture != null ? capture : start;
    c.setForecastTime(start != null ? start : c.CaptureTime);
    c.Interval = Duration.ofMinutes(5);
    c.header = "HDF5 " + getString(dsWhat, "prodname");
  }

  private static double unsigned(Object value) {
    if (value instanceof Short) {
      return ((Short) value) & 0xFFFF;
    }
    if (value instanceof Byte) {
      return ((Byte) value) & 0xFF;
    }
    if (value instanceof Integer) {
      return ((Integer) value).longValue() & 0xFFFFFFFFL;
    }
    return ((Number) value).doubleValue();
  }

  private static ZonedDateTime parseTime(String date, String time) {
    if (date == null || time == null) {
      return null;
    }
    return LocalDateTime.parse(date + time, TIME_FORMAT)
        .atZone(ZoneId.of("Z"));
  }

  private static Object attr(Node node, String name) {
    if (node == null) {
      return null;
    }
    Attribute a = node.getAttribute(name);
    return a == null ? null : a.getData();
  }

  private static double getDouble(Node node, String name, double def) {
    Object o = attr(node, name);
    if (o == null) {
      return def;
    }
    if (o.getClass().isArray()) {
      o = Array.get(o, 0);
    }
    return o instanceof Number ? ((Number) o).doubleValue()
        : Double.parseDouble(o.toString());
  }

  private static String getString(Node node, String name) {
    Object o = attr(node, name);
    if (o == null) {
      return null;
    }
    if (o.getClass().isArray()) {
      o = Array.get(o, 0);
    }
    return o.toString().trim();
  }
}
