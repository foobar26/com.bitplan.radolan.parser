package com.bitplan.radolan;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.bitplan.geo.DPoint;
import cs.fau.de.since.radolan.Composite;

import java.io.InputStream;
import java.time.ZoneOffset;

import org.junit.Test;

/**
 * tests reading of the HDF5 composites DWD delivers since 2024
 */
public class TestHdf5Composite {

  private Composite read(String name) throws Throwable {
    InputStream in = getClass().getResourceAsStream("/" + name);
    return new Composite(in);
  }

  @Test
  public void testHdf5() throws Throwable {
    Composite c = read("composite_wn_000.hd5");
    assertEquals(1100, c.getGridWidth());
    assertEquals(1200, c.getGridHeight());
    assertEquals(1200, c.PlainData.length);
    assertEquals(1100, c.PlainData[0].length);
    assertEquals("2026-10-24T21:50", c.getCaptureTime().withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime().toString());
    // value checked with h5py: raw 25942 * gain + offset
    assertEquals(12.0017f, c.getValue(550, 600), 0.001f);
    assertEquals(-32.5f, c.getValue(0, 0), 0.0f);
  }

  @Test
  public void testPrediction() throws Throwable {
    Composite c = read("composite_wn_030.hd5");
    assertEquals(30, java.time.Duration.between(c.getCaptureTime(), c.getForecastTime()).toMinutes());
  }

  @Test
  public void testProjection() throws Throwable {
    Composite c = read("composite_wn_000.hd5");
    // corner of the grid is the upper left corner given in the file
    DPoint ll = c.translateGridToLatLon(new DPoint(0, 0));
    assertEquals(55.86, ll.x, 0.05);
    assertEquals(1.46, ll.y, 0.05);
    // Berlin lies inside the grid and round trips
    DPoint grid = c.translateLatLonToGrid(52.52, 13.405);
    DPoint back = c.translateGridToLatLon(grid);
    assertEquals(52.52, back.x, 0.01);
    assertTrue(grid.x > 0 && grid.x < 1100 && grid.y > 0 && grid.y < 1200);
  }
}
