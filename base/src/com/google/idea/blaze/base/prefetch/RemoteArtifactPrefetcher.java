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
package com.google.idea.blaze.base.prefetch;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.idea.blaze.base.command.buildresult.OutputArtifact;
import com.google.idea.blaze.base.command.buildresult.RemoteOutputArtifact;
import com.intellij.openapi.components.ServiceManager;
import java.io.File;
import java.util.Collection;

/** A service for fetching a batch of remote files */
public interface RemoteArtifactPrefetcher {
  static RemoteArtifactPrefetcher getInstance() {
    return ServiceManager.getService(RemoteArtifactPrefetcher.class);
  }

  /**
   * Pick {@link RemoteOutputArtifact} from a list of {@link OutputArtifact} and fetch its content.
   * Only load content into memory if loadIntoMemory is true. Return {@link ListenableFuture} to
   * indicate whether all files has been fetched/ any of them failed with exception.
   */
  ListenableFuture<?> prefetchFiles(
      File cacheDir, Collection<? extends OutputArtifact> outputArtifacts, boolean loadIntoMemory);
}
