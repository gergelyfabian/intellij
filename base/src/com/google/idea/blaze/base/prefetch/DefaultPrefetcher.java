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

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.idea.blaze.base.command.buildresult.OutputArtifact;
import com.google.idea.blaze.base.command.buildresult.RemoteOutputArtifact;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Implementation of {@link RemoteArtifactPrefetcher}. Fetching files via {@link
 * RemoteOutputArtifact#prefetch}.
 */
public class DefaultPrefetcher implements RemoteArtifactPrefetcher {
  @Override
  public ListenableFuture<?> prefetchFiles(
      File cacheDir, Collection<? extends OutputArtifact> outputArtifacts, boolean loadIntoMemory) {
    if (!loadIntoMemory) {
      return Futures.immediateFuture(null);
    }
    List<ListenableFuture<?>> futures = new ArrayList<>();
    for (OutputArtifact file : outputArtifacts) {
      if (file instanceof RemoteOutputArtifact) {
        futures.add(FetchExecutor.EXECUTOR.submit(((RemoteOutputArtifact) file)::prefetch));
      }
    }
    return Futures.allAsList(futures);
  }
}
