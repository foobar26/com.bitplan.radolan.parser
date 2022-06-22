package com.bitplan.radolan;

import com.bitplan.geo.DPoint;
import cs.fau.de.since.radolan.Composite;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.lang.StringUtils;

import static org.junit.Assert.*;

public class TestComposite {

/*    @Test
    public void testCompositeWXRecent() throws Throwable {
        System.out.println("---Test WX Recent---");
        Composite composite = new Composite("https://opendata.dwd.de/weather/radar/composit/wx/raa01-wx_10000-latest-dwd---bin");
        System.out.print(composite.getCaptureTime().plusHours(2) + ": ");
        testPositionForComposite(composite, null);
    }
*/
    @Test
    public void testCompositeWX() throws Throwable {
        System.out.println("---Test WX Recent---");
        File wxFile = new File(
                "src/test/data/wx/raa01-wx_10000-2007240725-dwd---bin");
        assertTrue(wxFile.exists());
        Composite composite = new Composite(new FileInputStream(wxFile));
        System.out.print(composite.getCaptureTime().plusHours(2) + ": ");
        testPositionForComposite(composite, 9.5F);
        wxFile = new File(
                "src/test/data/wx/raa01-wx_10000-2007240915-dwd---bin");
        assertTrue(wxFile.exists());
        composite = new Composite(new FileInputStream(wxFile));
        System.out.print(composite.getCaptureTime().plusHours(2) + ": ");
        testPositionForComposite(composite, -32.5F);
        // todo check why this gets -94.5
        wxFile = new File(
                "src/test/data/wx/raa01-wx_10000-2007252215-dwd---bin");
        assertTrue(wxFile.exists());
        composite = new Composite(new FileInputStream(wxFile));
        System.out.print(composite.getCaptureTime().plusHours(2) + ": ");
        testPositionForComposite(composite, 33.5F);

    }

    @Test
    public void testCompositeFX() throws Throwable {
        System.out.println("---Test FX---");
        File fxFile = new File(
                "src/test/data/fx/FX2007240715.tar.bz2");
        assertTrue(fxFile.exists());
        InputStream inputStream = new FileInputStream(fxFile);
        byte[] lbytes = IOUtils.toByteArray(inputStream);
        inputStream.close();
        ByteArrayInputStream bin = new ByteArrayInputStream(lbytes);
        Map<Integer, Float> expectedValues = new HashMap<>();
        expectedValues.put(0, -32.5F);
        expectedValues.put(5, 9.5F);
        expectedValues.put(10, 12.0F);
        expectedValues.put(15, 20.95F);
        expectedValues.put(20, 12.5F);
        expectedValues.put(25, 18.45F);
        expectedValues.put(30, 14.5F);
        expectedValues.put(35, 2.0F);
        expectedValues.put(40, 4.9000015F);
        expectedValues.put(45, -32.5F);
        expectedValues.put(50, -32.5F);
        expectedValues.put(55, -32.5F);
        expectedValues.put(60, -32.5F);
        expectedValues.put(65, -32.5F);
        expectedValues.put(70, -32.5F);
        expectedValues.put(75, -32.5F);
        expectedValues.put(80, -32.5F);
        expectedValues.put(85, -32.5F);
        expectedValues.put(90, -32.5F);
        expectedValues.put(95, -32.5F);
        expectedValues.put(100, -32.5F);
        expectedValues.put(105, -32.5F);
        expectedValues.put(110, -32.5F);
        expectedValues.put(115, -32.5F);
        expectedValues.put(120, -32.5F);
        testTarBz(bin, expectedValues);
    }
/*
    @Test
    public void testCompositeFXRecent() throws Throwable{
        System.out.println("---Test FX Recent---");
        InputStream inputStream = new URL("https://opendata.dwd.de/weather/radar/composit/fx/FX_LATEST.tar.bz2").openStream();
        testFX(inputStream, null);
    }

*/
    @Test
    public void testCompositeRVRecent() throws Throwable {
        System.out.println("---Test RV Recent---");
        InputStream inputStream = new URL("https://opendata.dwd.de/weather/radar/composit/rv/DE1200_RV_LATEST.tar.bz2").openStream();
        testTarBzRV(inputStream, null);
    }

    @Test
    public void testCompositeWNRecent() throws Throwable {
        System.out.println("---Test WN Recent---");
        InputStream inputStream = new URL("https://opendata.dwd.de/weather/radar/composit/wn/WN_LATEST.tar.bz2").openStream();
        testTarBz(inputStream, null);
    }

    @Test
    public void testCompositeWN() throws Throwable {
        System.out.println("---Test WN---");
        File fxFile = new File(
                "src/test/data/wn/WN_LATEST.tar.bz2");
        assertTrue(fxFile.exists());
        InputStream inputStream = new FileInputStream(fxFile);
        byte[] lbytes = IOUtils.toByteArray(inputStream);
        inputStream.close();
        ByteArrayInputStream bin = new ByteArrayInputStream(lbytes);
        Map<Integer, Float> expectedValues = new HashMap<>();
        expectedValues.put(0, 25.7F);
        expectedValues.put(5, 26.7F);
        expectedValues.put(10, 25.900002F);
        expectedValues.put(15, 25.2F);
        expectedValues.put(20, 20.2F);
        expectedValues.put(25, 22.3F);
        expectedValues.put(30, 20.0F);
        expectedValues.put(35, 20.3F);
        expectedValues.put(40, 18.8F);
        expectedValues.put(45, 15.599998F);
        expectedValues.put(50, 16.2F);
        expectedValues.put(55, 15.200001F);
        expectedValues.put(60, 20.0F);
        expectedValues.put(65, 13.599998F);
        expectedValues.put(70, 5.9000015F);
        expectedValues.put(75, 7.700001F);
        expectedValues.put(80, 15.299999F);
        expectedValues.put(85, 5.700001F);
        expectedValues.put(90, -32.5F);
        expectedValues.put(95, -32.5F);
        expectedValues.put(100, -32.5F);
        expectedValues.put(105, -32.5F);
        expectedValues.put(110, 12.799999F);
        expectedValues.put(115, 10.0F);
        expectedValues.put(120, -32.5F);
        testTarBz(bin, expectedValues);
    }

    private void testBz(InputStream inputStream, Float expectedValue) throws Throwable {
        BZip2CompressorInputStream gzipIn = new BZip2CompressorInputStream(inputStream);
        int BUFFER_SIZE = 5000;
        byte[] buffer = new byte[BUFFER_SIZE];
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        BufferedOutputStream bufout = new BufferedOutputStream(bout, BUFFER_SIZE);
        int count = 0;
        while ((count = gzipIn.read(buffer, 0, BUFFER_SIZE)) != -1) {
            bufout.write(buffer, 0, count);
        }
        bufout.close();
        bout.close();
        ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
        Composite comp = new Composite(bin);
        testPositionForComposite(comp, expectedValue);
        bin.close();
        inputStream.close();
        gzipIn.close();
    }

    private void testTarBz(InputStream inputStream, Map<Integer, Float> expectedValues) throws Throwable {
        BZip2CompressorInputStream gzipIn = new BZip2CompressorInputStream(inputStream);
        int BUFFER_SIZE = 5000;
        byte[] buffer = new byte[BUFFER_SIZE];
        try (TarArchiveInputStream tarIn = new TarArchiveInputStream(gzipIn)) {
            TarArchiveEntry entry;
            while ((entry = (TarArchiveEntry) tarIn.getNextEntry()) != null) {
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                BufferedOutputStream bufout = new BufferedOutputStream(bout, BUFFER_SIZE);
                System.out.println("Entry: " + entry.getName());
                String[] parts = StringUtils.split(entry.getName(), "_");
                int filePrediction = Integer.parseInt(parts[1]);
                LocalDateTime ldt = LocalDateTime.ofInstant(KnownUrl.shortFormat.parse(entry.getName().substring(2,12)).toInstant(),
                        ZoneOffset.of("+4"));
                ldt = ldt.plusMinutes(filePrediction);
                System.out.print(ldt.toString() + ": ");
                int count = 0;
                while ((count = tarIn.read(buffer, 0, BUFFER_SIZE)) != -1) {
                    bufout.write(buffer, 0, count);
                }
                bufout.close();
                bout.close();
                ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
                Composite comp = new Composite(bin);
                Float expectedValue = null;
                if (expectedValues != null)
                    expectedValue = expectedValues.get(filePrediction);
                testPositionForComposite(comp, expectedValue);
                bin.close();
            }
        }
        inputStream.close();
        gzipIn.close();
    }
    private void testTarBzRV(InputStream inputStream, Map<Integer, Float> expectedValues) throws Throwable {
        BZip2CompressorInputStream gzipIn = new BZip2CompressorInputStream(inputStream);
        int BUFFER_SIZE = 5000;
        byte[] buffer = new byte[BUFFER_SIZE];
        try (TarArchiveInputStream tarIn = new TarArchiveInputStream(gzipIn)) {
            TarArchiveEntry entry;
            while ((entry = (TarArchiveEntry) tarIn.getNextEntry()) != null) {
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                BufferedOutputStream bufout = new BufferedOutputStream(bout, BUFFER_SIZE);
                System.out.println("Entry: " + entry.getName());
                String[] parts = StringUtils.split(entry.getName(), "_");
                int filePrediction = Integer.parseInt(parts[2]);
                LocalDateTime ldt = LocalDateTime.ofInstant(KnownUrl.shortFormat.parse(entry.getName().substring(9,19)).toInstant(),
                        ZoneOffset.of("+4"));
                ldt = ldt.plusMinutes(filePrediction);
                System.out.print(ldt.toString() + ": ");
                int count = 0;
                while ((count = tarIn.read(buffer, 0, BUFFER_SIZE)) != -1) {
                    bufout.write(buffer, 0, count);
                }
                bufout.close();
                bout.close();
                ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
                Composite comp = new Composite(bin);
                Float expectedValue = null;
                if (expectedValues != null)
                    expectedValue = expectedValues.get(filePrediction);
                testPositionForComposite(comp, expectedValue);
                bin.close();
            }
        }
        inputStream.close();
        gzipIn.close();
    }

    private void testPositionForComposite(Composite composite, Float testValue) {
        DPoint location = new DPoint(50.87483608732879, 6.099723069648566);
        Float valueNew = composite.getValueAtCoord(location);
        System.out.println(valueNew);
        if (testValue == null)
            assertNotNull("Value at position should not be null", valueNew);
        else
            assertEquals("Expected dbZ not found", testValue, valueNew);
    }
}
