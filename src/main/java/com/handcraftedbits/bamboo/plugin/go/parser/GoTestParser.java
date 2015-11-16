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
package com.handcraftedbits.bamboo.plugin.go.parser;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.handcraftedbits.bamboo.plugin.go.model.PackageTestResults;
import com.handcraftedbits.bamboo.plugin.go.model.SingleTestResult;
import com.handcraftedbits.bamboo.plugin.go.model.TestStatus;
import org.apache.commons.io.IOUtils;

public final class GoTestParser {
     private static final Pattern patternPackageFinish = Pattern.compile("^(\\?   |ok  |FAIL)\t([^\t]+)\t(.*)$");
     private static final Pattern patternTestFinish = Pattern.compile("^--- (FAIL|PASS|SKIP): ([^ ]+) \\(([^s]+)s\\)$");

     private GoTestParser () {
     }

     public static List<PackageTestResults> parseTests (final InputStream input) throws Exception {
          final List<String> lines = IOUtils.readLines(input, "UTF-8");
          final List<PackageTestResults> packageTestResults = new LinkedList<>();
          final Stack<SingleTestResult> testResults = new Stack<>();

          for (final String line : lines) {
               // A test has finished.  Parse it out and push it on the stack until we know which package it belongs to.

               if (line.startsWith("---")) {
                    final Matcher matcher = GoTestParser.patternTestFinish.matcher(line);

                    if (matcher.matches()) {
                         TestStatus status = null;

                         switch (matcher.group(1)) {
                              case "FAIL": {
                                   status = TestStatus.FAILED;

                                   break;
                              }

                              case "PASS": {
                                   status = TestStatus.PASSED;

                                   break;
                              }

                              case "SKIP": {
                                   status = TestStatus.SKIPPED;

                                   break;
                              }
                         }

                         if (status != null) {
                              testResults.push(new SingleTestResult(matcher.group(2), status, Double.parseDouble
                                   (matcher.group(3))));
                         }
                    }
               }

               // This is either noise or a finished set of package tests.

               else {
                    final Matcher matcher = GoTestParser.patternPackageFinish.matcher(line);

                    // We have a finished set of package tests, so create the model for it and clear the stack of tests.

                    if (matcher.matches()) {
                         final PackageTestResults packageResults = new PackageTestResults(matcher.group(2));

                         // In this case, either the go test run did not specify -v or there are no tests in the
                         // package.  We'll create a single "AllTests" test and assign it the correct status.  In the
                         // case of no tests existing for the package, the status will be "skipped".

                         if (testResults.empty()) {
                              double duration = 0.0d;
                              final String durationStr = matcher.group(3);
                              TestStatus testStatus = TestStatus.SKIPPED;

                              switch (matcher.group(1)) {
                                   case "FAIL": {
                                        testStatus = TestStatus.FAILED;

                                        break;
                                   }

                                   case "ok  ": {
                                        testStatus = TestStatus.PASSED;

                                        break;
                                   }
                              }

                              // If there are tests in the package, we should be able to extract the duration.

                              if (durationStr.endsWith("s")) {
                                   duration = Double.parseDouble(durationStr.substring(0, durationStr.length() - 1));
                              }

                              packageResults.addTestResult(new SingleTestResult("AllTests", testStatus, duration));
                         }

                         while (!testResults.empty()) {
                              packageResults.addTestResult(testResults.pop());
                         }

                         packageTestResults.add(packageResults);
                    }
               }
          }

          IOUtils.closeQuietly(input);

          return packageTestResults;
     }
}
