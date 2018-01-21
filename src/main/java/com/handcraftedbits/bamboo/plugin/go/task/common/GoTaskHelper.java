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
package com.handcraftedbits.bamboo.plugin.go.task.common;

import com.atlassian.bamboo.process.EnvironmentVariableAccessor;
import com.atlassian.bamboo.task.TaskContext;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityContext;
import com.atlassian.struts.TextProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class GoTaskHelper {
     private final CapabilityContext capabilityContext;
     private final EnvironmentVariableAccessor environmentVariableAccessor;
     private final TextProvider textProvider;

     GoTaskHelper (@NotNull final CapabilityContext capabilityContext,
          @NotNull final EnvironmentVariableAccessor environmentVariableAccessor,
          @NotNull final TextProvider textProvider) {
          this.capabilityContext = capabilityContext;
          this.environmentVariableAccessor = environmentVariableAccessor;
          this.textProvider = textProvider;
     }

     @NotNull
     public ProcessHelper createProcessHelper (@NotNull final TaskContext taskContext) {
          return new ProcessHelper(taskContext);
     }

     @Nullable
     public String getCapabilityValue (@NotNull final String name) {
          return this.capabilityContext.getCapabilityValue(name);
     }

     @NotNull
     public EnvironmentVariableAccessor getEnvironmentVariableAccessor () {
          return this.environmentVariableAccessor;
     }

     @Nullable
     public String getText (@NotNull final String key, @Nullable final String... values) {
          return this.textProvider.getText(key, values);
     }
}
