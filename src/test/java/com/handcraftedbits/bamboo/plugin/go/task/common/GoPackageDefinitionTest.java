/**
 * Copyright (C) 2015-2016 HandcraftedBits
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.handcraftedbits.bamboo.plugin.go.task.common;

import java.util.HashSet;

import org.testng.Assert;
import org.testng.annotations.Test;

public final class GoPackageDefinitionTest {
     @Test
     public void testPackageDefinition () {
          final GoPackageDefinition packageDefinition = new GoPackageDefinition(0, "  ./...  ", "  -flag  ");

          Assert.assertEquals(2, packageDefinition.getCommandLine(null).size());
          Assert.assertEquals(0, packageDefinition.getIndex());
          Assert.assertEquals("./...", packageDefinition.getName());
          Assert.assertEquals("./...", packageDefinition.getCommandLine(null).get(0));
          Assert.assertEquals("-flag", packageDefinition.getCommandLine(null).get(1));
     }

     @Test
     public void testPackageDefinitionEmpty () {
          final GoPackageDefinition packageDefinition = new GoPackageDefinition(0, "  ./...  ", "  ");

          Assert.assertEquals(1, packageDefinition.getCommandLine(null).size());
          Assert.assertEquals(0, packageDefinition.getIndex());
          Assert.assertEquals("./...", packageDefinition.getName());
          Assert.assertEquals("./...", packageDefinition.getCommandLine(null).get(0));
     }

     @Test
     public void testPackageDefinitionExclude () {
          final HashSet<String> excluded = new HashSet<>();
          final GoPackageDefinition packageDefinition = new GoPackageDefinition(0, "  ./...  ", "  -v  -flag  ");

          excluded.add("-v");

          Assert.assertEquals(2, packageDefinition.getCommandLine(excluded).size());
          Assert.assertEquals(0, packageDefinition.getIndex());
          Assert.assertEquals("./...", packageDefinition.getName());
          Assert.assertEquals("./...", packageDefinition.getCommandLine(null).get(0));
          Assert.assertEquals("-flag", packageDefinition.getCommandLine(excluded).get(1));
     }
}
