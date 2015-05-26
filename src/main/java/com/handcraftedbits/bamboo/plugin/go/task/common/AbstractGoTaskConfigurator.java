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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.atlassian.bamboo.collections.ActionParametersMap;
import com.atlassian.bamboo.process.EnvironmentVariableAccessor;
import com.atlassian.bamboo.task.AbstractTaskConfigurator;
import com.atlassian.bamboo.task.TaskDefinition;
import com.atlassian.bamboo.task.TaskRequirementSupport;
import com.atlassian.bamboo.utils.error.ErrorCollection;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityContext;
import com.atlassian.bamboo.v2.build.agent.capability.Requirement;
import com.atlassian.bamboo.v2.build.agent.capability.RequirementImpl;
import com.opensymphony.xwork2.TextProvider;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractGoTaskConfigurator extends AbstractTaskConfigurator implements TaskRequirementSupport {
     private final HashMap<String, ParameterInfo> parameters;
     private final HashSet<String> requirements;
     private final GoTaskHelper taskHelper;

     protected AbstractGoTaskConfigurator (@NotNull final CapabilityContext capabilityContext,
          @NotNull final EnvironmentVariableAccessor environmentVariableAccessor,
          @NotNull final TextProvider textProvider) {
          this.parameters = new HashMap<>();
          this.requirements = new HashSet<>();
          this.taskHelper = new GoTaskHelper(capabilityContext, environmentVariableAccessor, textProvider);
     }

     protected void addParameter (@NotNull final String name, @NotNull final Object defaultValue) {
          this.parameters.put(name, new ParameterInfo(defaultValue));
     }

     protected void addParameter (@NotNull final String name, @NotNull final Object defaultValue,
          @NotNull final String errorMessage) {
          this.parameters.put(name, new ParameterInfo(defaultValue, errorMessage));
     }

     protected void addRequirement (@NotNull final String name) {
          this.requirements.add(name);
     }

     @NotNull
     @Override
     public Set<Requirement> calculateRequirements (@NotNull final TaskDefinition taskDefinition) {
          final HashSet<Requirement> requirements = new HashSet<>();

          for (final String requirement : this.requirements) {
               requirements.add(new RequirementImpl(requirement, true, ".*"));
          }

          return requirements;
     }

     @NotNull
     @Override
     public Map<String, String> generateTaskConfigMap (@NotNull final ActionParametersMap params,
          @Nullable final TaskDefinition previousTaskDefinition) {
          final Map<String, String> config = super.generateTaskConfigMap(params, previousTaskDefinition);

          for (final String paramName : this.parameters.keySet()) {
               config.put(paramName, params.getString(paramName));
          }

          return config;
     }

     @NotNull
     GoTaskHelper getTaskHelper () {
          return this.taskHelper;
     }

     @Override
     public void populateContextForCreate (@NotNull final Map<String, Object> context) {
          super.populateContextForCreate(context);

          for (final String paramName : this.parameters.keySet()) {
               context.put(paramName, this.parameters.get(paramName).getDefaultValue());
          }
     }

     @Override
     public void populateContextForEdit (@NotNull final Map<String, Object> context,
          @NotNull final TaskDefinition taskDefinition) {
          super.populateContextForEdit(context, taskDefinition);

          for (final String paramName : this.parameters.keySet()) {
               context.put(paramName, taskDefinition.getConfiguration().get(paramName));
          }
     }

     @Override
     public void validate (@NotNull final ActionParametersMap params, @NotNull final ErrorCollection errorCollection) {
          super.validate(params, errorCollection);

          for (final String paramName : this.parameters.keySet()) {
               final ParameterInfo parameterInfo = this.parameters.get(paramName);

               if (parameterInfo.isRequired() && StringUtils.isEmpty(params.getString(paramName))) {
                    errorCollection.addError(paramName, this.taskHelper.getText(parameterInfo.getErrorMessage()));
               }
          }
     }

     private final class ParameterInfo {
          private final Object defaultValue;
          private final String errorMessage;
          private final boolean required;

          private ParameterInfo (@NotNull final Object defaultValue) {
               this.defaultValue = defaultValue;
               this.errorMessage = null;
               this.required = false;
          }

          private ParameterInfo (@NotNull final Object defaultValue, @NotNull final String errorMessage) {
               this.defaultValue = defaultValue;
               this.errorMessage = errorMessage;
               this.required = true;
          }

          @NotNull
          private Object getDefaultValue () {
               return this.defaultValue;
          }

          @NotNull
          private String getErrorMessage () {
               return this.errorMessage;
          }

          private boolean isRequired () {
               return this.required;
          }
     }
}
