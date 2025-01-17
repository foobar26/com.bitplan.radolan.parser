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
package com.bitplan.geo;

/**
 * Integer point helper class
 * @author wf
 *
 */
public class IPoint {
  public int x;
  public int y;
  
  public IPoint(int x,int y) {
    this.x=x;
    this.y=y;
  }

  /**
   * convert me from a Double Point
   * @param dp
   */
  public IPoint(DPoint dp) {
    this.x=(int) Math.round(dp.x);
    this.y=(int) Math.round(dp.y);
  }
  
  /**
   * get the distance to another point
   * @param ip
   * @return - the distance
   */
  public double dist(IPoint ip) {
    if (ip==null)
      return Double.NaN;
    double xd = x - ip.x;
    double yd = y - ip.y;
    double dist=Math.sqrt(xd * xd + yd * yd);
    return dist;
  }

}
