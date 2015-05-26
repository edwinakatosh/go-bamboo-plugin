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
package com.handcraftedbits.bamboo.plugin.go.task.parser;

import com.atlassian.bamboo.process.EnvironmentVariableAccessor;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityContext;
import com.handcraftedbits.bamboo.plugin.go.task.common.AbstractGoTaskConfigurator;
import com.opensymphony.xwork2.TextProvider;
import org.jetbrains.annotations.NotNull;

public final class GoTestParserTaskConfigurator extends AbstractGoTaskConfigurator {
     public GoTestParserTaskConfigurator (@NotNull final CapabilityContext capabilityContext,
          @NotNull final EnvironmentVariableAccessor environmentVariableAccessor,
          @NotNull final TextProvider textProvider) {
          super(capabilityContext, environmentVariableAccessor, textProvider);

          // Add parameters.

          addParameter(GoTestParserTaskConfiguration.PARAM_PATTERN, "_goTestOutput/*.log",
               "task.parser.field.pattern.error");
     }
}
