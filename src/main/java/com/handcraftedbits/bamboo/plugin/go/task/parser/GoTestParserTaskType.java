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

import java.io.File;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.atlassian.bamboo.build.test.TestCollationService;
import com.atlassian.bamboo.build.test.TestCollectionResult;
import com.atlassian.bamboo.build.test.TestCollectionResultBuilder;
import com.atlassian.bamboo.build.test.TestReportCollector;
import com.atlassian.bamboo.process.EnvironmentVariableAccessor;
import com.atlassian.bamboo.task.TaskContext;
import com.atlassian.bamboo.task.TaskException;
import com.atlassian.bamboo.task.TaskResult;
import com.atlassian.bamboo.task.TaskResultBuilder;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityContext;
import com.atlassian.struts.TextProvider;
import com.handcraftedbits.bamboo.plugin.go.model.PackageTestResults;
import com.handcraftedbits.bamboo.plugin.go.parser.GoTestParser;
import com.handcraftedbits.bamboo.plugin.go.task.common.AbstractGoTaskType;
import org.jetbrains.annotations.NotNull;

public final class GoTestParserTaskType extends AbstractGoTaskType {
     private final TestCollationService testCollationService;

     public GoTestParserTaskType (@NotNull final CapabilityContext capabilityContext,
          @NotNull final EnvironmentVariableAccessor environmentVariableAccessor,
          @NotNull final TextProvider textProvider, @NotNull final TestCollationService testCollationService) {
          super(capabilityContext, environmentVariableAccessor, textProvider);

          this.testCollationService = testCollationService;
     }

     @NotNull
     @Override
     public TaskResult execute (@NotNull final TaskContext taskContext) throws TaskException {
          final GoTestParserTaskConfiguration configuration = new GoTestParserTaskConfiguration(getTaskHelper(),
               taskContext);

          this.testCollationService.collateTestResults(taskContext, configuration.getResultFilePattern(),
               new GoTestReportCollector());

          return TaskResultBuilder.newBuilder(taskContext).checkTestFailures().build();
     }

     private class GoTestReportCollector implements TestReportCollector {
          @NotNull
          @Override
          public TestCollectionResult collect (@NotNull final File file) throws Exception {
               final TestCollectionResultBuilder builder = new TestCollectionResultBuilder();
               final List<PackageTestResults> testResults = GoTestParser.parseTests(new FileInputStream(file));

               // Iterate over all the package test results and combine them into a merged TestCollectionResult.

               for (final PackageTestResults packageTestResults : testResults) {
                    final TestCollectionResult curResult = packageTestResults.toTestCollectionResult();

                    builder.addFailedTestResults(curResult.getFailedTestResults())
                         .addSkippedTestResults(curResult.getSkippedTestResults())
                         .addSuccessfulTestResults(curResult.getSuccessfulTestResults());
               }

               return builder.build();
          }

          @NotNull
          @Override
          public Set<String> getSupportedFileExtensions () {
               return new HashSet<>();
          }
     }
}
