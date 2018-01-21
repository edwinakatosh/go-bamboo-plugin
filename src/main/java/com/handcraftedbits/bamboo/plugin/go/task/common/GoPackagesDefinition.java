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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.atlassian.bamboo.collections.ActionParametersMap;

public final class GoPackagesDefinition implements Iterable<GoPackageDefinition> {
     private static final String PARAM_FLAGS_PREFIX = "_gbp_flags_";
     private static final String PARAM_INDICES = "_gbp_indices";
     private static final String PARAM_PACKAGENAME_PREFIX = "_gbp_packageName_";
     private static final String PARAM_PACKAGES = "_gbp_packages";

     private final List<GoPackageDefinition> packages;

     public GoPackagesDefinition () {
          this.packages = new ArrayList<>();
     }

     public GoPackagesDefinition (@NotNull final ActionParametersMap params) {
          this(params.getParameters());
     }

     public GoPackagesDefinition (@NotNull final Map<String, ?> configuration) {
          final Set<Integer> indices;
          final String indicesStr;

          this.packages = new ArrayList<>();

          // We expect the submitted form to contain a field with comma-delimited widget indices and a corresponding
          // packageName and flags field. Maintaining this order is important for validation purposes.

          indicesStr = getParameter(configuration.get(GoPackagesDefinition.PARAM_INDICES));

          if (indicesStr == null) {
               // Incorrectly-formatted form submission, so return.

               return;
          }

          indices = parseIndices(indicesStr);

          for (final Integer index : indices) {
               final String flags = getParameter(configuration.get(GoPackagesDefinition.PARAM_FLAGS_PREFIX + index));
               final String packageName = getParameter(configuration.get(GoPackagesDefinition.PARAM_PACKAGENAME_PREFIX +
                    index));

               addPackageDefinition(index, (packageName == null) ? "" : packageName, (flags == null) ? "" : flags);
          }
     }

     public void addPackageDefinition (final int index, @NotNull final String packageName,
          @NotNull final String flags) {
          if (this.packages.size() > 0) {
               this.packages.get(this.packages.size() - 1).setLast(false);
          }

          this.packages.add(new GoPackageDefinition(index, packageName, flags));

          this.packages.get(this.packages.size() - 1).setLast(true);
     }

     public void addToConfig (@NotNull final Map<String, String> context) {
          context.put(GoPackagesDefinition.PARAM_INDICES, getIndices());

          for (final GoPackageDefinition packageDefinition : this.packages) {
               context.put(GoPackagesDefinition.PARAM_PACKAGENAME_PREFIX + packageDefinition.getIndex(),
                    packageDefinition.getName());
               context.put(GoPackagesDefinition.PARAM_FLAGS_PREFIX + packageDefinition.getIndex(),
                    packageDefinition.getFlags());
          }
     }

     public void addToContext (@NotNull final Map<String, Object> context) {
          context.put(GoPackagesDefinition.PARAM_INDICES, getIndices());
          context.put(GoPackagesDefinition.PARAM_PACKAGES, this.packages);
     }

     @NotNull
     private String getIndices () {
          final StringBuilder result = new StringBuilder();

          for (int i = 0; i < this.packages.size(); ++i) {
               result.append(this.packages.get(i).getIndex());

               if (i < (this.packages.size() - 1)) {
                    result.append(",");
               }
          }

          return result.toString();
     }

     @Nullable
     private String getParameter (@Nullable final Object value) {
          if (value == null) {
               return null;
          }

          if (value instanceof String) {
               return ((String) value);
          }

          return ((String[]) value)[0];
     }

     @Override
     @NotNull
     public Iterator<GoPackageDefinition> iterator () {
          return this.packages.iterator();
     }

     @NotNull
     private Set<Integer> parseIndices (@NotNull final String indicesStr) {
          final Set<Integer> indices = new TreeSet<>();
          final String[] splitIndices = indicesStr.split(",");

          for (final String index : splitIndices) {
               try {
                    indices.add(Integer.parseInt(index));
               }

               catch (final NumberFormatException e) {
                    // Ignore.
               }
          }

          return indices;
     }
}
