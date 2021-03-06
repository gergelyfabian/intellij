/*
 * Copyright 2020 The Bazel Authors. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.tools.idea.run.tasks;

import com.android.tools.idea.run.LaunchOptions;
import com.google.common.collect.ImmutableMap;
import com.intellij.openapi.project.Project;
import java.io.File;
import java.util.List;

/** Compat class for {@link DeployTask} */
public class DeployTasksCompat {
  private DeployTasksCompat() {}

  // #api4.0 : Constructor signature changed in 4.1
  public static LaunchTask createDeployTask(
      Project project,
      ImmutableMap<String, List<File>> filesToInstall,
      LaunchOptions launchOptions) {
    // We don't have a device information, fallback to the most conservative
    // install option.
    return new DeployTask(
        project,
        filesToInstall,
        launchOptions.getPmInstallOptions(/*device=*/ null),
        launchOptions.getInstallOnAllUsers());
  }
}
