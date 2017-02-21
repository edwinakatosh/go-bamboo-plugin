/**
 * Copyright (C) 2015-2017 HandcraftedBits
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

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class GoPackageDefinition implements Serializable {
     private static final long serialVersionUID = 1L;

     private final String flags;
     private final int index;
     private boolean last;
     private final String name;

     GoPackageDefinition (final int index, @NotNull final String name, @NotNull final String flags) {
          this.flags = flags;
          this.index = index;
          this.last = false;
          this.name = name.trim();
     }

     @NotNull
     public List<String> getCommandLine (@Nullable final Set<String> flagsToExclude) {
          final List<String> result = new LinkedList<>();

          result.add(this.name);
          result.addAll(parseFlags(flagsToExclude));

          return result;
     }

     @NotNull
     public String getFlags () {
          return this.flags;
     }

     public int getIndex () {
          return this.index;
     }

     @NotNull
     public String getName () {
          return this.name;
     }

     public boolean isLast () {
          return this.last;
     }

     @NotNull
     private List<String> parseFlags (@Nullable final Set<String> flagsToExclude) {
          final LinkedList<String> result = new LinkedList<>();
          final String[] tokens;

          if (this.flags.trim().length() == 0) {
               return result;
          }

          tokens = this.flags.trim().split("\\s+");

          if (tokens.length > 0) {
               for (final String token : tokens) {
                    if (flagsToExclude == null) {
                         result.add(token);
                    }

                    else {
                         if (!flagsToExclude.contains(token)) {
                              result.add(token);
                         }
                    }
               }
          }

          return result;
     }

     public void setLast (final boolean last) {
          this.last = last;
     }
}
