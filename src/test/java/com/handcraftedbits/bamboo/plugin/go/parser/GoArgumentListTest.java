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

import org.junit.Assert;
import org.junit.Test;

public final class GoArgumentListTest {
     @Test
     public void testArgumentList () {
          final GoArgumentList list = new GoArgumentList("  ./...   -flag  ");

          Assert.assertEquals(2, list.getItems().size());
          Assert.assertEquals("./...", list.getItems().get(0));
          Assert.assertEquals("-flag", list.getItems().get(1));
     }

     @Test
     public void testArgumentListComparison () {
          GoArgumentList listA = new GoArgumentList("   ");
          GoArgumentList listB = new GoArgumentList("");

          Assert.assertEquals(listA, listB);

          Assert.assertNotEquals(listA, "");

          listA = new GoArgumentList("./... -v");
          listB = new GoArgumentList("./... -flag");

          Assert.assertNotEquals(listA, listB);

          listA = new GoArgumentList("./... -v");
          listB = new GoArgumentList("pkg -v");

          Assert.assertNotEquals(listA, listB);
     }

     @Test
     public void testArgumentListEmpty () {
          final GoArgumentList list = new GoArgumentList("    ");

          Assert.assertEquals(0, list.getItems().size());
     }

     @Test
     public void testArgumentListExclude () {
          final HashSet<String> excluded = new HashSet<>();
          final GoArgumentList list;

          excluded.add("-v");

          list = new GoArgumentList("  ./... -v  -flag ", excluded);

          Assert.assertEquals(2, list.getItems().size());
          Assert.assertEquals("./...", list.getItems().get(0));
          Assert.assertEquals("-flag", list.getItems().get(1));
     }

     @Test
     public void testArgumentListToString () {
          final GoArgumentList list = new GoArgumentList(" ./... -v   \t  -flag  ");

          Assert.assertEquals("./... -v -flag", list.toString());
     }
}
