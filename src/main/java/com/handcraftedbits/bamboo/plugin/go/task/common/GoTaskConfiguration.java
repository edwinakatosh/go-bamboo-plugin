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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.atlassian.bamboo.task.TaskContext;
import com.handcraftedbits.bamboo.plugin.go.capability.GoCapabilityTypeModule;

public class GoTaskConfiguration {
     private final TaskContext taskContext;
     private final GoTaskHelper taskHelper;

     public GoTaskConfiguration (@NotNull final GoTaskHelper taskHelper, @NotNull final TaskContext taskContext) {
          this.taskContext = taskContext;
          this.taskHelper = taskHelper;
     }

     @Nullable
     public String getGoExecutable () {
          return this.taskHelper.getCapabilityValue(GoCapabilityTypeModule.CAPABILITY_KEY_GO);
     }

     @Nullable
     public String getGodepExecutable () {
          return this.taskHelper.getCapabilityValue(GoCapabilityTypeModule.CAPABILITY_KEY_GODEP);
     }

     @NotNull
     public String getSourcePath () {
          return this.taskContext.getConfigurationMap().get(AbstractGoExecutableTaskConfigurator.CONFIG_SOURCE_PATH);
     }

     @NotNull
     protected TaskContext getTaskContext () {
          return this.taskContext;
     }

     GoTaskHelper getTaskHelper () {
          return this.taskHelper;
     }
}
