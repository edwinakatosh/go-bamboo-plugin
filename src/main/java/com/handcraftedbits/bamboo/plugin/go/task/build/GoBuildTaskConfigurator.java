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
package com.handcraftedbits.bamboo.plugin.go.task.build;

import com.atlassian.bamboo.process.EnvironmentVariableAccessor;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityContext;
import com.handcraftedbits.bamboo.plugin.go.capability.GoCapabilityTypeModule;
import com.handcraftedbits.bamboo.plugin.go.task.common.AbstractGoExecutableTaskConfigurator;
import com.handcraftedbits.bamboo.plugin.go.task.common.GoPackageAwareTaskConfiguration;
import com.opensymphony.xwork2.TextProvider;
import org.jetbrains.annotations.NotNull;

public final class GoBuildTaskConfigurator extends AbstractGoExecutableTaskConfigurator {
     public GoBuildTaskConfigurator (@NotNull final CapabilityContext capabilityContext,
          @NotNull final EnvironmentVariableAccessor environmentVariableAccessor,
          @NotNull final TextProvider textProvider) {
          super(capabilityContext, environmentVariableAccessor, textProvider, true);

          // Add requirements.

          addRequirement(GoCapabilityTypeModule.CAPABILITY_KEY_GO);

          // Add parameters.

          addParameter(GoPackageAwareTaskConfiguration.PARAM_PACKAGES, "./...", "task.test.build.packages.error");
     }
}
