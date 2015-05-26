/*
 * #%L
 * Go Plugin for Bamboo
 * %%
 * Copyright (C) 2015 HandcraftedBits
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.handcraftedbits.bamboo.plugin.go.parser;

import java.util.HashSet;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public final class GoArgumentParserTest {
     @Test
     public void testParseArguments () {
          final List<GoArgumentList> arguments = GoArgumentParser.parseArguments("./... -flag \n  \n ./testPkg -v \n" +
               "./testPkg2 -flag2\n\n");

          Assert.assertEquals(3, arguments.size());
          Assert.assertEquals("./... -flag", arguments.get(0).toString());
          Assert.assertEquals("./testPkg -v", arguments.get(1).toString());
          Assert.assertEquals("./testPkg2 -flag2", arguments.get(2).toString());
     }

     @Test
     public void testParseArgumentsEmpty () {
          final List<GoArgumentList> arguments = GoArgumentParser.parseArguments("");

          Assert.assertEquals(0, arguments.size());
     }

     @Test
     public void testParseArgumentsExclude () {
          final List<GoArgumentList> arguments;
          final HashSet<String> flagsToExclude = new HashSet<>();

          flagsToExclude.add("-v");

          arguments = GoArgumentParser.parseArguments("./... -v -flag -v \n./testPkg -v", flagsToExclude);

          Assert.assertEquals(2, arguments.size());
          Assert.assertEquals("./... -flag", arguments.get(0).toString());
          Assert.assertEquals("./testPkg", arguments.get(1).toString());
     }
}
