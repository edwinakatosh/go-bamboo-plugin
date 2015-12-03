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
package com.handcraftedbits.bamboo.plugin.go.model;

import java.util.LinkedList;
import java.util.List;

import com.atlassian.bamboo.build.test.TestCollectionResult;
import com.atlassian.bamboo.build.test.TestCollectionResultBuilder;
import com.atlassian.bamboo.results.tests.TestResults;
import com.atlassian.bamboo.resultsummary.tests.TestState;
import edu.emory.mathcs.backport.java.util.Collections;
import org.jetbrains.annotations.NotNull;

public final class PackageTestResults {
     private final String name;
     private final LinkedList<SingleTestResult> testResults;

     public PackageTestResults (@NotNull final String fullName) {
          this.testResults = new LinkedList<>();

          // Bamboo expects a Java-like package name, so replace '.' with '_'.

          this.name = fullName.replace('.', '_');
     }

     public void addTestResult (@NotNull final SingleTestResult testResult) {
          this.testResults.add(0, testResult);
     }

     public String getName () {
          return this.name;
     }

     @SuppressWarnings("unchecked")
     public List<SingleTestResult> getTestResults () {
          return Collections.unmodifiableList(this.testResults);
     }

     @NotNull
     public TestCollectionResult toTestCollectionResult () {
          final LinkedList<TestResults> failedTests = new LinkedList<>();
          final LinkedList<TestResults> skippedTests = new LinkedList<>();
          final LinkedList<TestResults> successfulTests = new LinkedList<>();

          for (final SingleTestResult testResult : this.testResults) {
               switch (testResult.getStatus()) {
                    case FAILED: {
                         final TestResults testResults = new TestResults(this.name, testResult.getName(), "" +
                              testResult.getDuration());

                         testResults.setState(TestState.FAILED);

                         failedTests.add(testResults);

                         break;
                    }

                    case PASSED: {
                         final TestResults testResults = new TestResults(this.name, testResult.getName(), "" +
                              testResult.getDuration());

                         testResults.setState(TestState.SUCCESS);

                         successfulTests.add(testResults);

                         break;
                    }

                    case SKIPPED: {
                         final TestResults testResults = new TestResults(this.name, testResult.getName(), "" +
                              testResult.getDuration());

                         testResults.setState(TestState.SKIPPED);

                         skippedTests.add(testResults);

                         break;
                    }
               }
          }

          return new TestCollectionResultBuilder().addFailedTestResults(failedTests).addSkippedTestResults(skippedTests)
               .addSuccessfulTestResults(successfulTests).build();
     }
}
