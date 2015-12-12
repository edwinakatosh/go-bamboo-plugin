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
import java.io.IOException;
import java.util.LinkedList;

import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import com.atlassian.bamboo.process.EnvironmentVariableAccessor;
import com.atlassian.bamboo.task.TaskContext;
import com.atlassian.bamboo.task.TaskException;
import com.atlassian.bamboo.task.TaskResult;
import com.atlassian.bamboo.task.TaskResultBuilder;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityContext;
import com.atlassian.struts.TextProvider;
import com.atlassian.utils.process.ExternalProcess;
import com.handcraftedbits.bamboo.plugin.go.task.common.AbstractGoTaskType;
import com.handcraftedbits.bamboo.plugin.go.task.common.GoPackageDefinition;
import com.handcraftedbits.bamboo.plugin.go.task.common.GoPackagesDefinition;
import com.handcraftedbits.bamboo.plugin.go.task.common.ProcessHelper;

public final class GoTestTaskType extends AbstractGoTaskType {
     public GoTestTaskType (@NotNull final CapabilityContext capabilityContext,
          @NotNull final EnvironmentVariableAccessor environmentVariableAccessor,
          @NotNull final TextProvider textProvider) {
          super(capabilityContext, environmentVariableAccessor, textProvider);
     }

     @NotNull
     @Override
     public TaskResult execute (@NotNull final TaskContext taskContext) throws TaskException {
          final GoTestTaskConfiguration configuration = new GoTestTaskConfiguration(getTaskHelper(), taskContext);
          int i = 0;
          final File outputDirectory = new File(taskContext.getWorkingDirectory(), configuration.getLogOutputPath());
          final GoPackagesDefinition packages = configuration.getPackages();
          final ProcessHelper processHelper = getTaskHelper().createProcessHelper(taskContext);

          if (outputDirectory.exists()) {
               try {
                    FileUtils.forceDelete(outputDirectory);
               }

               catch (final IOException e) {
                    taskContext.getBuildLogger().addErrorLogEntry(getTaskHelper().getText("task.test.error.directory." +
                         "results.delete", outputDirectory.getAbsolutePath()));

                    return TaskResultBuilder.newBuilder(taskContext).failedWithError().build();
               }
          }

          if (!outputDirectory.mkdirs()) {
               taskContext.getBuildLogger().addErrorLogEntry(getTaskHelper().getText("task.test.error.directory." +
                    "results.create", outputDirectory.getAbsolutePath()));

               return TaskResultBuilder.newBuilder(taskContext).failedWithError().build();
          }

          for (final GoPackageDefinition pkg : packages) {
               final LinkedList<String> commandLine = new LinkedList<>();
               final FileOutputHandler fileOutputHandler = new FileOutputHandler(new File(outputDirectory,
                    "goTestOutput" + (i++) + ".log"));
               final ExternalProcess process;

               commandLine.add(configuration.getGoExecutable());
               commandLine.add("test");
               commandLine.add("-v");
               commandLine.addAll(pkg.getCommandLine(GoTestTaskConfiguration.flagsToExclude));

               if (configuration.shouldLogOutputToBuild()) {
                    process = processHelper.executeProcess(commandLine, configuration.getSourcePath(),
                         configuration.getEnvironmentVariables(), new CompositeOutputHandler(
                              taskContext.getBuildLogger(), processHelper.createStandardOutputHandler(),
                              fileOutputHandler),
                         new CompositeOutputHandler(taskContext.getBuildLogger(),
                              processHelper.createStandardErrorHandler(), fileOutputHandler));
               }

               else {
                    process = processHelper.executeProcess(commandLine, configuration.getSourcePath(),
                         configuration.getEnvironmentVariables(), fileOutputHandler, fileOutputHandler);
               }

               if (process.getHandler().getExitCode() != 0) {
                    return TaskResultBuilder.newBuilder(taskContext).checkReturnCode(process, 0).build();
               }
          }

          return TaskResultBuilder.newBuilder(taskContext).success().build();
     }
}
