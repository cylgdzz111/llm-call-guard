package io.github.cuiyunlong.llmguard.core.singleflight;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

public class DefaultSingleflight implements Singleflight {
  private final ConcurrentHashMap<String, CompletableFuture<?>> inflight = new ConcurrentHashMap<>();

  @Override
  public <T> SingleflightResult<T> execute(String key, Callable<T> supplier) throws Exception {
    CompletableFuture<T> future = new CompletableFuture<>();
    @SuppressWarnings("unchecked")
    CompletableFuture<T> existing = (CompletableFuture<T>) inflight.putIfAbsent(key, future);
    if (existing != null) {
      try {
        return new SingleflightResult<>(existing.get(), true);
      } catch (ExecutionException e) {
        Throwable cause = e.getCause();
        if (cause instanceof Exception) {
          throw (Exception) cause;
        }
        throw new Exception(cause);
      }
    }

    try {
      T result = supplier.call();
      future.complete(result);
      return new SingleflightResult<>(result, false);
    } catch (Throwable t) {
      future.completeExceptionally(t);
      if (t instanceof Exception) {
        throw (Exception) t;
      }
      throw new Exception(t);
    } finally {
      inflight.remove(key, future);
    }
  }
}
