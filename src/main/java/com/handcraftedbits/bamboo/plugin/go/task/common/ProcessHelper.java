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

import java.io.File;
import java.util.List;
import java.util.Map;

import com.atlassian.bamboo.process.ErrorStreamToBuildLoggerOutputHandler;
import com.atlassian.bamboo.process.StreamToBuildLoggerOutputHandler;
import com.atlassian.bamboo.task.TaskContext;
import com.atlassian.utils.process.ExternalProcess;
import com.atlassian.utils.process.ExternalProcessBuilder;
import com.atlassian.utils.process.OutputHandler;
import com.handcraftedbits.bamboo.plugin.go.util.Constants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ProcessHelper {
     private final TaskContext taskContext;

     ProcessHelper (@NotNull final TaskContext taskContext) {
          this.taskContext = taskContext;
     }

     public OutputHandler createStandardErrorHandler () {
          return new ErrorStreamToBuildLoggerOutputHandler(this.taskContext.getBuildLogger(), this.taskContext
               .getCommonContext().getResultKey());
     }

     public OutputHandler createStandardOutputHandler () {
          return new StreamToBuildLoggerOutputHandler(this.taskContext.getBuildLogger(), this.taskContext
               .getCommonContext().getResultKey());
     }

     public ExternalProcess executeProcess (@NotNull final List<String> commandLine,
          @Nullable final String workingDirectory) {
          return executeProcess(commandLine, workingDirectory, null);
     }

     public ExternalProcess executeProcess (@NotNull final List<String> commandLine,
          @Nullable final String workingDirectory, @Nullable final Map<String, String> environment) {
          return executeProcess(commandLine, workingDirectory, environment, createStandardOutputHandler(),
               createStandardErrorHandler());
     }

     public ExternalProcess executeProcess (@NotNull final List<String> commandLine,
          @Nullable final String workingDirectory, @Nullable final Map<String, String> environment,
          @NotNull final OutputHandler stdoutHandler, @NotNull final OutputHandler stderrHandler) {
          final ExternalProcessBuilder builder = new ExternalProcessBuilder().env(Constants.EnvironmentVariable.GOPATH,
               this.taskContext.getWorkingDirectory().getAbsolutePath()).env(Constants.EnvironmentVariable.GOROOT,
                    this.taskContext.getConfigurationMap().get(AbstractGoExecutableTaskConfigurator.CONFIG_GOROOT));
          final ExternalProcess process;

          if (workingDirectory != null) {
               builder.command(commandLine, new File(this.taskContext.getWorkingDirectory(), workingDirectory));
          }

          else {
               builder.command(commandLine, this.taskContext.getWorkingDirectory());
          }

          // Add any extra environment variables.

          if (environment != null) {
               builder.env(environment);
          }

          // Add output handlers.

          builder.handlers(stdoutHandler, stderrHandler);

          // Create and execute the process.

          process = builder.build();

          process.execute();

          return process;
     }
}
