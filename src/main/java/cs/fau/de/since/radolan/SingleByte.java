/**
 * Copyright (c) 2018 BITPlan GmbH
 *
 * http://www.bitplan.com
 *
 * This file is part of the Opensource project at:
 * https://github.com/BITPlan/com.bitplan.radolan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Parts which are derived from https://gitlab.cs.fau.de/since/radolan are also
 * under MIT license.
 */
package cs.fau.de.since.radolan;

/**
 * migrated to Java from https://gitlab.cs.fau.de/since/radolan/blob/master/singlebyte.go
 * @author wf
 *
 */
public class SingleByte {

  // parseSingleByte parses the single byte encoded composite as described in [1] and writes
  // into the previously created PlainData field of the composite.
  public static void parseSingleByte(Composite c) {
    int last = c.PlainData.length - 1;
    for (int y = 0; y < c.PlainData.length; y++) {
      byte[] yline = readRowSingleByte(c, y);
      try {
        int yt = last - y; // write vertically flipped
        int yw = c.PlainData[last - y].length;
        decodeSingleByte(c, yt, yw, yline);
      } catch (Exception e) {
        c.error = e;
        break;
      }
    }
  }

  // readRowSingleByte reads a line until horizontal limit from the given reader
  // This method is used to get a line of single byte encoded data.
  private static byte[] readRowSingleByte(Composite c, int y) {
    byte[] yrow = new byte[c.getDx()];
    for (int x = 0; x < c.getDx(); x++) {
      yrow[x] = c.getByte(x, y);
    }
    return yrow;
  }

  // decodeSingleByte decodes the source line and writes to the given destination.
  private static void decodeSingleByte(Composite c, int y, int xw, byte[] line) throws Exception {
    if (xw != line.length) {
      throw new Exception(String.format(
              "decodeSingleByte destination size %d and source size %d are not even or equal",
              xw, line.length));
    }
    for (int x = 0; x < xw; x++) {
      float value = rvp6SingleByte(c, line[x]);
      c.setValue(x, y, value);
    }
  }

  // rvp6SingleByte converts the raw byte of single byte encoded
  // composite products to radar video processor values (rvp-6). NaN may be returned
  // when the no-data flag is set.
  private static float rvp6SingleByte(Composite c, byte value) {
    if (value == 250) {
      return Float.NaN;
    }

    float conv = (float) c.rvp6Raw(value & 0xff);  // set decimal point

    if (c.getDataUnit() != Catalog.Unit.Unit_dBZ) {
      return conv;
    }

    return Conversion.toDBZ(conv);
  }

/*
  func (c *Composite) readLineSingleByte(rd *bufio.Reader) (line []byte, err error) {
    line = make([]byte, c.Dx)
    _, err = io.ReadFull(rd, line)
    if err != nil {
      err = newError("readLineSingleByte", err.Error())
    }
    return
  }

  func (c *Composite) decodeSingleByte(dst []float32, line []byte) error {
    if len(dst) != len(line) {
      return newError("decodeSingleByte", "wrong destination or source size")
    }

    for i, v := range line {
      dst[i] = c.rvp6SingleByte(v)
    }

    return nil
  }

  // rvp6SingleByte converts the raw byte of single byte encoded
  // composite products to radar video processor values (rvp-6). NaN may be returned
  // when the no-data flag is set.
  func (c *Composite) rvp6SingleByte(value byte) float32 {
    if value == 250 { // error code: no-data
      return NaN
    }

    conv := c.rvp6Raw(int(value)) // set decimal point

    // not sure if single byte formats are even used for other things than dBZ (RX, dBZ)
    if c.DataUnit != Unit_dBZ {
      return conv
    }

    return toDBZ(conv)
  }
*/
}
