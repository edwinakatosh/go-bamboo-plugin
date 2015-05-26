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
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public final class GoArgumentList {
     private final List<String> items;

     GoArgumentList (final String argLine) {
          this(argLine, null);
     }

     GoArgumentList (final String argLine, final Set<String> flagsToExclude) {
          if (flagsToExclude == null) {
               this.items = parseArgLine(argLine, new HashSet<String>());
          }

          else {
               this.items = parseArgLine(argLine, flagsToExclude);
          }
     }

     @Override
     public boolean equals (final Object obj) {
          return ((obj instanceof GoArgumentList) && this.items.equals(((GoArgumentList) obj).items));
     }

     public List<String> getItems () {
          return this.items;
     }

     private List<String> parseArgLine (final String argLine, final Set<String> flagsToExclude) {
          final LinkedList<String> result = new LinkedList<>();
          final String tokens[];

          if (argLine.trim().length() == 0) {
               return result;
          }

          tokens = argLine.trim().split("\\s+");

          if (tokens.length > 0) {
               for (final String token : tokens) {
                    if (!flagsToExclude.contains(token)) {
                         result.add(token);
                    }
               }
          }

          return result;
     }

     @Override
     public String toString () {
          return String.join(" ", this.items);
     }
}
