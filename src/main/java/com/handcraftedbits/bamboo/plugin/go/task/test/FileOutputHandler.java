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
package com.handcraftedbits.bamboo.plugin.go.task.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.atlassian.utils.process.BaseOutputHandler;
import com.atlassian.utils.process.ProcessException;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

final class FileOutputHandler extends BaseOutputHandler {
     private final File file;

     FileOutputHandler (@NotNull final File file) {
          this.file = file;
     }

     @Override
     public void process (@NotNull final InputStream output) throws ProcessException {
          try (final FileOutputStream fileOutput = new FileOutputStream(this.file, true)) {
        	  IOUtils.copy(output, fileOutput);
          }

          catch (final IOException e) {
               throw new ProcessException(e);
          }
     }
}
