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

import java.util.Map;

import com.atlassian.bamboo.collections.ActionParametersMap;
import com.atlassian.bamboo.process.EnvironmentVariableAccessor;
import com.atlassian.bamboo.task.TaskDefinition;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityContext;
import com.atlassian.struts.TextProvider;
import com.handcraftedbits.bamboo.plugin.go.capability.GoCapabilityTypeModule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractPackageAwareTaskConfigurator extends AbstractGoExecutableTaskConfigurator {
     protected AbstractPackageAwareTaskConfigurator (@NotNull final CapabilityContext capabilityContext,
          @NotNull final EnvironmentVariableAccessor environmentVariableAccessor,
          @NotNull final TextProvider textProvider, final boolean allowCustomEnvironment) {
          super(capabilityContext, environmentVariableAccessor, textProvider, allowCustomEnvironment);

          // Add requirements.

          addRequirement(GoCapabilityTypeModule.CAPABILITY_KEY_GO);
     }

     @Override
     @NotNull
     public Map<String, String> generateTaskConfigMap (@NotNull final ActionParametersMap params,
          @Nullable final TaskDefinition previousTaskDefinition) {
          final Map<String, String> config = super.generateTaskConfigMap(params, previousTaskDefinition);

          new GoPackagesDefinition(params).addToConfig(config);

          return config;
     }

     @Override
     public void populateContextForCreate (@NotNull final Map<String, Object> context) {
          final GoPackagesDefinition packages = new GoPackagesDefinition();

          super.populateContextForCreate(context);

          packages.addPackageDefinition(0, "", "");

          packages.addToContext(context);
     }

     @Override
     public void populateContextForEdit (@NotNull final Map<String, Object> context,
          @NotNull final TaskDefinition taskDefinition) {
          super.populateContextForEdit(context, taskDefinition);

          new GoPackagesDefinition(taskDefinition.getConfiguration()).addToContext(context);
     }
}
