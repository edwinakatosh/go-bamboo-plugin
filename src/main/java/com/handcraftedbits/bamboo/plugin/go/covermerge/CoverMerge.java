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

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public final class CoverMerge {
     private CoverMerge () {
     }

     private static void extractCoveredFiles (final Document document, final Map<String, String> fileContents) {
          final Elements elements;

          // Find the dropdown, which contains a mapping of element IDs to filenames.  The form we're looking for is:
          // <option value="id">filename.go (xx.x%)</option>

          elements = document.select("select#files > option");

          for (final Element element : elements) {
               final Element contents;
               String filename = element.text();
               final String id = element.attr("value");
               final int index = filename.indexOf(".go");

               if (index == -1) {
                    filename = "<unknown>.go";
               }

               // Now, find the <pre> block with corresponding ID; this will contain the file contents (and coverage
               // information).

               contents = document.select("pre#" + id).first();

               if (contents == null) {
                    fileContents.put(filename, "<unable to find coverage information for file>");
               }

               else {
                    fileContents.put(filename, contents.html());
               }
          }
     }

     public static String mergeCoverageFiles (final InputStream... inputStreams) throws IOException {
          int count = 0;
          final Element divElement;
          final Document documents[] = new Document[inputStreams.length];
          final Map<String, String> fileContents = new TreeMap<>();
          final Element selectElement;

          for (int i = 0; i < inputStreams.length; ++i) {
               documents[i] = Jsoup.parse(inputStreams[i], "UTF-8", "");
          }

          if (inputStreams.length == 0) {
               return documents[0].toString();
          }

          // For each coverage file, extract the covered files from the HTML and sort them by name.

          for (final Document document : documents) {
               CoverMerge.extractCoveredFiles(document, fileContents);
          }

          // Reuse the first document by stripping out the existing <select> options and <pre> code blocks.  Then we'll
          // add in the merged set of files.

          divElement = documents[0].select("div#content").first();
          selectElement = documents[0].select("select#files").first();

          if ((divElement == null) || (selectElement == null)) {
               return documents[0].toString();
          }

          divElement.children().remove();
          selectElement.children().remove();

          for (final String key : fileContents.keySet()) {
               divElement.append(String.format("<pre class=\"file\" id=\"file%d\"%s>%s\n</pre>", count,
                    count == 0 ? "" : "style=\"display:none\"", fileContents.get(key)));
               selectElement.append(String.format("<option value=\"file%d\">%s</option>\n", count++, key));
          }

          return documents[0].toString();
     }
}
