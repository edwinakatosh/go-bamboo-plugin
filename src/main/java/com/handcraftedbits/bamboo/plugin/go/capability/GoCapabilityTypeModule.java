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
package com.handcraftedbits.bamboo.plugin.go.capability;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import com.atlassian.bamboo.v2.build.agent.capability.AbstractExecutableCapabilityTypeModule;
import com.atlassian.bamboo.v2.build.agent.capability.AbstractMultipleExecutableCapabilityTypeModule;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityDefaultsHelper;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilitySet;
import com.google.common.collect.Lists;
import com.handcraftedbits.bamboo.plugin.go.util.Constants;

public final class GoCapabilityTypeModule extends AbstractMultipleExecutableCapabilityTypeModule {
     public static final String CAPABILITY_KEY_GO = "capability.go";
     public static final String CAPABILITY_KEY_GODEP = "capability.go.godep";
     private static final String EXECUTABLE_KIND_KEY = "goExecutableKind";

     private final LinkedList<CapabilityDefaultsHelper> capabilityDefaultsHelpers;

     public GoCapabilityTypeModule () {
          this.capabilityDefaultsHelpers = new LinkedList<>();

          this.capabilityDefaultsHelpers.add(new GoCapabilityDefaultsHelper());
          this.capabilityDefaultsHelpers.add(new GodepCapabilityDefaultsHelper());
     }

     @NotNull
     @Override
     public CapabilitySet addDefaultCapabilities (@NotNull final CapabilitySet capabilitySet) {
          // Override since the default implementation will just look for the Go executable on the PATH, and we want to
          // consider GOROOT/GOPATH for it and other binaries.

          for (final CapabilityDefaultsHelper helper : this.capabilityDefaultsHelpers) {
               helper.addDefaultCapabilities(capabilitySet);
          }

          return capabilitySet;
     }

     @Override
     public List<String> getAdditionalCapabilityKeys () {
          return Lists.newArrayList(GoCapabilityTypeModule.CAPABILITY_KEY_GODEP);
     }

     @Override
     public String getCapabilityKindUndefinedKey () {
          return "capability.go.error.undefinedExecutableKind";
     }

     @Override
     public String getCapabilityUndefinedKey () {
          return "capability.go.error.undefinedExecutable";
     }

     @Override
     public List<String> getDefaultWindowPaths () {
          return new LinkedList<>();
     }

     @Override
     public String getExecutableDescription (final String key) {
          return getText(
               AbstractExecutableCapabilityTypeModule.AGENT_CAPABILITY_TYPE_PREFIX + key + ".field.description");
     }

     @Override
     public String getExecutableFilename () {
          return Constants.Executable.GO;
     }

     @Override
     public String getExecutableKindKey () {
          return GoCapabilityTypeModule.EXECUTABLE_KIND_KEY;
     }

     @Override
     public String getMandatoryCapabilityKey () {
          return GoCapabilityTypeModule.CAPABILITY_KEY_GO;
     }

     @NotNull
     @Override
     public Map<String, String> validate (@NotNull final Map<String, String[]> params) {
          final String[] executableKind = params.get(GoCapabilityTypeModule.EXECUTABLE_KIND_KEY);
          final String[] executableValue;
          final Map<String, String> result = super.validate(params);

          // Override since the default implementation doesn't handle multiple executable types.

          if (executableKind == null) {
               return result;
          }

          executableValue = params.get(executableKind[0]);

          if (executableValue == null) {
               return result;
          }

          if (StringUtils.isEmpty(executableValue[0])) {
               result.put(GoCapabilityTypeModule.CAPABILITY_KEY_GO,
                    getText(executableKind[0] + ".error.undefinedExecutable"));
          }

          return result;
     }
}
