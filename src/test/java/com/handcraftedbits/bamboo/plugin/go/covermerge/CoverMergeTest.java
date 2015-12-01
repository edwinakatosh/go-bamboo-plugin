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
package com.handcraftedbits.bamboo.plugin.go.covermerge;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.testng.Assert;
import org.testng.annotations.Test;

public final class CoverMergeTest {
     @Test
     public void testMergeMissingCodeBlock () throws Exception {
          final InputStream inputStreams[] = new InputStream[] { getCoverOutputResource("error_missingCodeBlock.html"),
               getCoverOutputResource("strings.html") };

          try {
               final Elements codeBlocks;
               final String merged = CoverMerge.mergeCoverageFiles(inputStreams);

               codeBlocks = Jsoup.parse(merged).select("pre");

               Assert.assertEquals(6, codeBlocks.size());
               Assert.assertEquals("<unable to find coverage information for file>", codeBlocks.get(0).text());
          }

          finally {
               for (final InputStream inputStream : inputStreams) {
                    IOUtils.closeQuietly(inputStream);
               }
          }
     }

     @Test
     public void testMergeMissingContent () throws Exception {
          final InputStream inputStreams[] = new InputStream[] { getCoverOutputResource("error_missingContent.html"),
               getCoverOutputResource("strings.html") };
          final String originalContents;

          try (final InputStream inputStream = getCoverOutputResource("error_missingContent.html")) {
               originalContents = Jsoup.parse(inputStream, "UTF-8", "").toString();
          }

          try {
               Assert.assertEquals(originalContents, CoverMerge.mergeCoverageFiles(inputStreams));
          }

          finally {
               for (final InputStream inputStream : inputStreams) {
                    IOUtils.closeQuietly(inputStream);
               }
          }
     }

     @Test
     public void testMergeMissingFilename () throws Exception {
          final InputStream inputStreams[] = new InputStream[] { getCoverOutputResource("error_missingFilename.html"),
               getCoverOutputResource("strings.html") };

          try {
               final Elements filenames;
               final String merged = CoverMerge.mergeCoverageFiles(inputStreams);

               filenames = Jsoup.parse(merged).select("select#files > option");

               Assert.assertEquals(6, filenames.size());
               Assert.assertEquals("<unknown>.go", filenames.get(0).text());
               Assert.assertEquals("sort/sort.go (98.3%)", filenames.get(1).text());
          }

          finally {
               for (final InputStream inputStream : inputStreams) {
                    IOUtils.closeQuietly(inputStream);
               }
          }
     }

     @Test
     public void testMergeMissingFiles () throws Exception {
          final InputStream inputStreams[] = new InputStream[] { getCoverOutputResource("error_missingFiles.html"),
               getCoverOutputResource("strings.html") };
          final String originalContents;

          try (final InputStream inputStream = getCoverOutputResource("error_missingFiles.html")) {
               originalContents = Jsoup.parse(inputStream, "UTF-8", "").toString();
          }

          try {
               Assert.assertEquals(originalContents, CoverMerge.mergeCoverageFiles(inputStreams));
          }

          finally {
               for (final InputStream inputStream : inputStreams) {
                    IOUtils.closeQuietly(inputStream);
               }
          }
     }

     @Test
     public void testMergeSingleFile () throws Exception {
          final String originalContents;

          try (final InputStream inputStream = getCoverOutputResource("sort.html")) {
               originalContents = Jsoup.parse(inputStream, "UTF-8", "").toString();
          }

          try (final InputStream inputStream = getCoverOutputResource("sort.html")) {
               final String mergedContent = CoverMerge.mergeCoverageFiles(inputStream);

               Assert.assertEquals(originalContents, mergedContent);
          }
     }

     @Test
     public void testMergeSuccessful () throws Exception {
          final InputStream inputStreams[] = new InputStream[] { getCoverOutputResource("sort.html"),
               getCoverOutputResource("strings.html"), getCoverOutputResource("time.html") };

          try {
               final String expected = IOUtils.toString(getCoverOutputResource("merged.html"), "UTF-8");

               Assert.assertEquals(expected, CoverMerge.mergeCoverageFiles(inputStreams));
          }

          finally {
               for (final InputStream inputStream : inputStreams) {
                    IOUtils.closeQuietly(inputStream);
               }
          }
     }

     private InputStream getCoverOutputResource (final String name) {
          return getClass().getClassLoader().getResourceAsStream("goToolCoverOutput/" + name);
     }
}
