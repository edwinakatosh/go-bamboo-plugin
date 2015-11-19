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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.utils.process.BaseOutputHandler;
import com.atlassian.utils.process.OutputHandler;
import com.atlassian.utils.process.ProcessException;
import org.jetbrains.annotations.NotNull;

final class CompositeOutputHandler extends BaseOutputHandler {
     private final BuildLogger buildLogger;
     private final OutputHandler[] outputHandlers;

     CompositeOutputHandler (@NotNull final BuildLogger buildLogger, @NotNull final OutputHandler... outputHandlers) {
          this.buildLogger = buildLogger;
          this.outputHandlers = outputHandlers;
     }

     @Override
     public void process (@NotNull final InputStream output) throws ProcessException {
          final InputStreamWrapper wrappedOutput = new InputStreamWrapper(output);

          wrappedOutput.mark(Integer.MAX_VALUE);

          for (final OutputHandler outputHandler : this.outputHandlers) {
               try {
                    // Use a wrapped, buffered input stream so we can reuse it multiple times (it's likely that each
                    // output handler will close the input stream).

                    outputHandler.process(wrappedOutput);

                    wrappedOutput.reset();
               }

               catch (final IOException | ProcessException e) {
                    this.buildLogger.addErrorLogEntry("Error processing command output", e);
               }
          }

          try {
               wrappedOutput.realClose();
          }

          catch (final IOException e) {
               // Ignore.
          }
     }

     private static final class InputStreamWrapper extends BufferedInputStream {
          private InputStreamWrapper (@NotNull final InputStream inputStream) {
               super(inputStream);
          }

          @Override
          public void close () throws IOException {
          }

          private void realClose () throws IOException {
               super.close();
          }
     }
}
