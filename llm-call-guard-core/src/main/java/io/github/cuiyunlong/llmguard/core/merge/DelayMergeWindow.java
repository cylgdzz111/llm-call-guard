package io.github.cuiyunlong.llmguard.core.merge;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DelayMergeWindow implements MergeWindow {
  private final ConcurrentHashMap<String, CompletableFuture<?>> pending = new ConcurrentHashMap<>();
  private final ScheduledExecutorService scheduler;

  public DelayMergeWindow() {
    this(Executors.newScheduledThreadPool(1, r -> {
      Thread t = new Thread(r, "llm-guard-merge");
      t.setDaemon(true);
      return t;
    }));
  }

  public DelayMergeWindow(ScheduledExecutorService scheduler) {
    this.scheduler = scheduler;
  }

  @Override
  public <T> MergeResult<T> merge(String key, long windowMs, Callable<T> supplier) throws Exception {
    CompletableFuture<T> future = new CompletableFuture<>();
    @SuppressWarnings("unchecked")
    CompletableFuture<T> existing = (CompletableFuture<T>) pending.putIfAbsent(key, future);
    if (existing != null) {
      try {
        return new MergeResult<>(existing.get(), true);
      } catch (ExecutionException e) {
        Throwable cause = e.getCause();
        if (cause instanceof Exception) {
          throw (Exception) cause;
        }
        throw new Exception(cause);
      }
    }

    scheduler.schedule(() -> {
      try {
        T result = supplier.call();
        future.complete(result);
      } catch (Throwable t) {
        future.completeExceptionally(t);
      } finally {
        pending.remove(key, future);
      }
    }, windowMs, TimeUnit.MILLISECONDS);

    try {
      return new MergeResult<>(future.get(), false);
    } catch (ExecutionException e) {
      Throwable cause = e.getCause();
      if (cause instanceof Exception) {
        throw (Exception) cause;
      }
      throw new Exception(cause);
    }
  }
}
