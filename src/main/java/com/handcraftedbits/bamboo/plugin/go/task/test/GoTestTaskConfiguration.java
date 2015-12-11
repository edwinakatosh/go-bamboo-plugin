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
package com.handcraftedbits.bamboo.plugin.go.task.test;

import java.util.HashSet;

import org.jetbrains.annotations.NotNull;

import com.atlassian.bamboo.task.TaskContext;
import com.handcraftedbits.bamboo.plugin.go.task.common.GoPackageAwareTaskConfiguration;
import com.handcraftedbits.bamboo.plugin.go.task.common.GoTaskHelper;

final class GoTestTaskConfiguration extends GoPackageAwareTaskConfiguration {
     static final String PARAM_LOG_OUTPUT_PATH = "logOutputPath";
     static final String PARAM_LOG_OUTPUT_TO_BUILD = "logOutputToBuild";

     static final HashSet<String> flagsToExclude = new HashSet<>();

     static {
          GoTestTaskConfiguration.flagsToExclude.add("-v");
     }

     GoTestTaskConfiguration (@NotNull final GoTaskHelper taskHelper, @NotNull final TaskContext taskContext) {
          super(taskHelper, taskContext);
     }

     @NotNull
     String getLogOutputPath () {
          return getTaskContext().getConfigurationMap().get(GoTestTaskConfiguration.PARAM_LOG_OUTPUT_PATH);
     }

     boolean shouldLogOutputToBuild () {
          return getTaskContext().getConfigurationMap().getAsBoolean(GoTestTaskConfiguration.PARAM_LOG_OUTPUT_TO_BUILD);
     }
}
