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

import cs.fau.de.since.radolan.Composite;

import java.io.File;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;

/**
 * Base class for tests
 * 
 * @author wf
 *
 */
public class BaseTest {
  public static boolean debug = false;

  protected static Logger LOGGER = Logger.getLogger("com.bitplan.radolan");

  String tmpDir = System.getProperty("java.io.tmpdir");

  /**
   * check if we are in the Travis-CI environment
   * 
   * @return true if Travis user was detected
   */
  public boolean isTravis() {
    String user = System.getProperty("user.name");
    return user.equals("travis");
  }
}
