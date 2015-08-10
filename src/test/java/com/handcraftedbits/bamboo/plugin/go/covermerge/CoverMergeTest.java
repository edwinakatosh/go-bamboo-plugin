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
import org.junit.Assert;
import org.junit.Test;

public final class CoverMergeTest {
     @Test
     public void testMergeSuccessful () throws Exception {
          final InputStream inputStreams[] = new InputStream[] {
               getCoverOutputResource("sort.html"),
               getCoverOutputResource("strings.html"),
               getCoverOutputResource("time.html")
          };

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
