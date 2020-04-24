package com.google.idea.blaze.base.prefetch;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.idea.blaze.base.command.buildresult.BlazeArtifact;
import com.google.idea.blaze.base.command.buildresult.OutputArtifact;
import com.google.idea.blaze.base.command.buildresult.RemoteOutputArtifact;
import com.google.idea.blaze.base.filecache.RemoteOutputsCache;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import java.util.Collection;

/** A service for fetching a list of {@link OutputArtifact} */
public class OutputArtifactPrefetcher {
  public static OutputArtifactPrefetcher getInstance() {
    return ServiceManager.getService(OutputArtifactPrefetcher.class);
  }

  /**
   * Invoke PrefetchService to fetch content for local files and RemoteArtifactPrefetcher to fetch
   * content for {@link RemoteOutputArtifact}. Return {@link ListenableFuture} to indicate the
   * fetching status for all services.
   */
  public ListenableFuture<?> prefetchFiles(
      Project project,
      Collection<? extends OutputArtifact> outputArtifacts,
      boolean loadIntoMemory) {
    ListenableFuture<?> fetchLocalFilesFuture =
        PrefetchService.getInstance()
            .prefetchFiles(
                /* files= */ BlazeArtifact.getLocalFiles(outputArtifacts),
                /* refetchCachedFiles= */ true,
                /* fetchFileTypes= */ false);
    ListenableFuture<?> fetchRemoteArtifactFuture =
        RemoteArtifactPrefetcher.getInstance()
            .prefetchFiles(
                /* cacheDir= */ RemoteOutputsCache.getCacheDir(project),
                /* outputArtifacts= */ outputArtifacts,
                /* loadIntoMemory= */ loadIntoMemory);
    return Futures.allAsList(fetchLocalFilesFuture, fetchRemoteArtifactFuture);
  }
}
