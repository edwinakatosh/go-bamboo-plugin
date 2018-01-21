/**
 * Copyright (C) 2015-2018 HandcraftedBits
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
package com.handcraftedbits.bamboo.plugin.go.model;

import org.jetbrains.annotations.NotNull;

public final class SingleTestResult {
     private final double duration;
     private final String name;
     private final TestStatus status;

     public SingleTestResult (@NotNull final String name, @NotNull final TestStatus status, final double duration) {
          this.duration = duration;
          this.name = name;
          this.status = status;
     }

     public double getDuration () {
          return this.duration;
     }

     public String getName () {
          return this.name;
     }

     public TestStatus getStatus () {
          return this.status;
     }
}
