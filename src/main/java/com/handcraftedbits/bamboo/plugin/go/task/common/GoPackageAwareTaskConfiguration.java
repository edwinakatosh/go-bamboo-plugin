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

import java.util.Map;

import com.atlassian.bamboo.task.TaskConfigConstants;
import com.atlassian.bamboo.task.TaskContext;
import org.jetbrains.annotations.NotNull;

public class GoPackageAwareTaskConfiguration extends GoTaskConfiguration {
     public GoPackageAwareTaskConfiguration (@NotNull final GoTaskHelper taskHelper,
          @NotNull final TaskContext taskContext) {
          super(taskHelper, taskContext);
     }

     @NotNull
     public Map<String, String> getEnvironmentVariables () {
          return getTaskHelper().getEnvironmentVariableAccessor().splitEnvironmentAssignments(getTaskContext()
               .getConfigurationMap().get(TaskConfigConstants.CFG_ENVIRONMENT_VARIABLES), false);
     }

     @NotNull
     public GoPackagesDefinition getPackages () {
          return new GoPackagesDefinition(getTaskContext().getConfigurationMap());
     }
}
