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
package com.bitplan.radolan;

import com.bitplan.geo.GeoProjection;
import cs.fau.de.since.radolan.Catalog.Unit;

import java.time.Duration;

/**
 * abstraction of the Radar Image
 * @author wf
 *
 */
public interface RadarImage extends GeoProjection {
  
  /**
   * get the value at the given grid position
   * @param x
   * @param y
   * @return - the value
   */
  public float getValue(int x, int y);
  
  
  /**
   * get the data unit of the image
   * @return - the data Unit
   */
  public Unit getDataUnit();
  
  /**
   * get the interval displayed
   * @return - the interval
   */
  public Duration getInterval();
  
  
}
