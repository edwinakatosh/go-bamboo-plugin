/**
 * Copyright (C) 2015-2016 HandcraftedBits
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
package com.handcraftedbits.bamboo.plugin.go.parser;

import java.io.InputStream;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.handcraftedbits.bamboo.plugin.go.model.PackageTestResults;
import com.handcraftedbits.bamboo.plugin.go.model.SingleTestResult;
import com.handcraftedbits.bamboo.plugin.go.model.TestStatus;

public final class GoTestParserTest {
     private InputStream getOutputResource (@NotNull final String name) {
          return getClass().getClassLoader().getResourceAsStream("goTestOutput/" + name);
     }

     @Test
     public void testOutputMixed () throws Exception {
          PackageTestResults packageTestResults;
          final List<PackageTestResults> results = GoTestParser.parseTests(getOutputResource("testOutputMixed"));
          SingleTestResult testResult;

          Assert.assertEquals(2, results.size());

          packageTestResults = results.get(0);

          Assert.assertEquals("github_com/handcraftedbits/go-bamboo/first", packageTestResults.getName());
          Assert.assertEquals(3, packageTestResults.getTestResults().size());

          testResult = packageTestResults.getTestResults().get(0);

          Assert.assertEquals("TestDoAFail", testResult.getName());
          Assert.assertEquals(TestStatus.FAILED, testResult.getStatus());
          Assert.assertEquals(0.0d, testResult.getDuration(), 0.0d);

          testResult = packageTestResults.getTestResults().get(1);

          Assert.assertEquals("TestDoAPass", testResult.getName());
          Assert.assertEquals(TestStatus.PASSED, testResult.getStatus());
          Assert.assertEquals(0.0d, testResult.getDuration(), 0.0d);

          testResult = packageTestResults.getTestResults().get(2);

          Assert.assertEquals("TestDoASkip", testResult.getName());
          Assert.assertEquals(TestStatus.SKIPPED, testResult.getStatus());
          Assert.assertEquals(0.0d, testResult.getDuration(), 0.0d);

          packageTestResults = results.get(1);

          Assert.assertEquals("github_com/handcraftedbits/go-bamboo/second", packageTestResults.getName());
          Assert.assertEquals(1, packageTestResults.getTestResults().size());

          testResult = packageTestResults.getTestResults().get(0);

          Assert.assertEquals("AllTests", testResult.getName());
          Assert.assertEquals(TestStatus.SKIPPED, testResult.getStatus());
          Assert.assertEquals(0.0d, testResult.getDuration(), 0.0d);
     }

     @Test
     public void testOutputNotVerbose () throws Exception {
          PackageTestResults packageTestResults;
          final List<PackageTestResults> results = GoTestParser.parseTests(getOutputResource("testOutputNotVerbose"));
          SingleTestResult testResult;

          Assert.assertEquals(4, results.size());

          packageTestResults = results.get(0);

          Assert.assertEquals("github_com/handcraftedbits/go-bamboo/first", packageTestResults.getName());
          Assert.assertEquals(1, packageTestResults.getTestResults().size());

          testResult = packageTestResults.getTestResults().get(0);

          Assert.assertEquals("AllTests", testResult.getName());
          Assert.assertEquals(TestStatus.PASSED, testResult.getStatus());
          Assert.assertEquals(2.0d, testResult.getDuration(), 0.0d);

          packageTestResults = results.get(1);

          Assert.assertEquals("github_com/handcraftedbits/go-bamboo/second", packageTestResults.getName());
          Assert.assertEquals(1, packageTestResults.getTestResults().size());

          testResult = packageTestResults.getTestResults().get(0);

          Assert.assertEquals("AllTests", testResult.getName());
          Assert.assertEquals(TestStatus.SKIPPED, testResult.getStatus());
          Assert.assertEquals(0.0d, testResult.getDuration(), 0.0d);

          packageTestResults = results.get(2);

          Assert.assertEquals("github_com/handcraftedbits/go-bamboo/third", packageTestResults.getName());
          Assert.assertEquals(1, packageTestResults.getTestResults().size());

          testResult = packageTestResults.getTestResults().get(0);

          Assert.assertEquals("TestDoC", testResult.getName());
          Assert.assertEquals(TestStatus.FAILED, testResult.getStatus());
          Assert.assertEquals(0.0d, testResult.getDuration(), 0.0d);

          packageTestResults = results.get(3);

          Assert.assertEquals("github_com/handcraftedbits/go-bamboo/fourth", packageTestResults.getName());
          Assert.assertEquals(1, packageTestResults.getTestResults().size());

          testResult = packageTestResults.getTestResults().get(0);

          Assert.assertEquals("AllTests", testResult.getName());
          Assert.assertEquals(TestStatus.FAILED, testResult.getStatus());
          Assert.assertEquals(0.008d, testResult.getDuration(), 0.0d);
     }

     @Test
     public void testOutputParallel () throws Exception {
          final PackageTestResults packageTestResults;
          final List<PackageTestResults> results = GoTestParser.parseTests(getOutputResource("testOutputParallel"));

          Assert.assertEquals(1, results.size());

          packageTestResults = results.get(0);

          Assert.assertEquals("github_com/handcraftedbits/go-bamboo/first", packageTestResults.getName());
          Assert.assertEquals(20, packageTestResults.getTestResults().size());

          for (final SingleTestResult testResult : packageTestResults.getTestResults()) {
               Assert.assertTrue(testResult.getName().startsWith("TestDoAPass"));
               Assert.assertEquals(TestStatus.PASSED, testResult.getStatus());
               Assert.assertEquals(1.0d, testResult.getDuration(), 0.0d);
          }
     }

     @Test
     public void testOutputSequential () throws Exception {
          final PackageTestResults packageTestResults;
          final List<PackageTestResults> results = GoTestParser.parseTests(getOutputResource("testOutputSequential"));

          Assert.assertEquals(1, results.size());

          packageTestResults = results.get(0);

          Assert.assertEquals("github_com/handcraftedbits/go-bamboo/first", packageTestResults.getName());
          Assert.assertEquals(20, packageTestResults.getTestResults().size());

          for (final SingleTestResult testResult : packageTestResults.getTestResults()) {
               Assert.assertTrue(testResult.getName().startsWith("TestDoAPass"));
               Assert.assertEquals(TestStatus.PASSED, testResult.getStatus());
               Assert.assertEquals(1.0d, testResult.getDuration(), 0.0d);
          }
     }
}
