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

import java.io.File;

import com.atlassian.bamboo.process.EnvironmentVariableAccessor;
import com.atlassian.bamboo.task.TaskConfigConstants;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityContext;
import com.atlassian.struts.TextProvider;
import com.handcraftedbits.bamboo.plugin.go.capability.GoCapabilityTypeModule;
import com.handcraftedbits.bamboo.plugin.go.util.Constants;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractGoExecutableTaskConfigurator extends AbstractGoTaskConfigurator {
     public static final String CONFIG_GOROOT = "goroot";
     public static final String CONFIG_SOURCE_PATH = "sourcePath";

     protected AbstractGoExecutableTaskConfigurator (@NotNull final CapabilityContext capabilityContext,
          @NotNull final EnvironmentVariableAccessor environmentVariableAccessor,
          @NotNull final TextProvider textProvider, final boolean allowCustomEnvironment) {
          super(capabilityContext, environmentVariableAccessor, textProvider);

          // Add common parameters.

          addParameter(AbstractGoExecutableTaskConfigurator.CONFIG_GOROOT, determineGoRoot(),
               "task.common.field.goroot.error");
          addParameter(AbstractGoExecutableTaskConfigurator.CONFIG_SOURCE_PATH, "",
               "task.common.field.sourcePath.error");

          if (allowCustomEnvironment) {
               addParameter(TaskConfigConstants.CFG_ENVIRONMENT_VARIABLES, "");
          }
     }

     @NotNull
     private String determineGoRoot () {
          final String goPath;
          final String goRootEnv = System.getenv(Constants.EnvironmentVariable.GOROOT);

          // Best case scenario: GOROOT is already defined in the environment.

          if (goRootEnv != null) {
               return goRootEnv.trim();
          }

          // See if the Go capability has been defined. If so, we can use <path_to_go_executable>/.. as a best guess
          // for the correct GOROOT path.

          goPath = getTaskHelper().getCapabilityValue(GoCapabilityTypeModule.CAPABILITY_KEY_GO);

          if (!StringUtils.isEmpty(goPath)) {
               File goFile = new File(goPath).getParentFile();

               if (goFile != null) {
                    goFile = goFile.getParentFile();

                    if (goFile != null) {
                         return goFile.getAbsolutePath();
                    }
               }
          }

          // Just return an empty string since we can't tell what GOROOT should be.

          return "";
     }
}
