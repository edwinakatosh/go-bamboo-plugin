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
package com.handcraftedbits.bamboo.plugin.go.task.common;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.atlassian.bamboo.task.TaskConfigConstants;
import com.atlassian.bamboo.task.TaskContext;
import com.handcraftedbits.bamboo.plugin.go.parser.GoArgumentList;
import com.handcraftedbits.bamboo.plugin.go.parser.GoArgumentParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GoPackageAwareTaskConfiguration extends GoTaskConfiguration {
     public static final String PARAM_PACKAGES = "packages";

     public GoPackageAwareTaskConfiguration (@NotNull final GoTaskHelper taskHelper,
          @NotNull final TaskContext taskContext) {
          super(taskHelper, taskContext);
     }

     @NotNull
     public Map<String, String> getEnvironmentVariables () {
          return getTaskHelper().getEnvironmentVariableAccessor().splitEnvironmentAssignments
               (getTaskContext().getConfigurationMap().get(TaskConfigConstants.CFG_ENVIRONMENT_VARIABLES), false);
     }

     @NotNull
     public List<GoArgumentList> getPackagesWithArguments (@Nullable final Set<String> flagsToExclude) {
          return GoArgumentParser.parseArguments(getTaskContext().getConfigurationMap().get
               (GoPackageAwareTaskConfiguration.PARAM_PACKAGES), flagsToExclude);
     }
}
