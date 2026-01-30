package io.github.cuiyunlong.llmguard.core.executor;

import io.github.cuiyunlong.llmguard.api.fallback.FallbackHandler;
import io.github.cuiyunlong.llmguard.api.model.CallContext;
import io.github.cuiyunlong.llmguard.api.model.EffectivePlan;
import io.github.cuiyunlong.llmguard.api.model.GuardResultMeta;
import io.github.cuiyunlong.llmguard.api.model.enums.GuardStatus;
import io.github.cuiyunlong.llmguard.core.exec.RetryRunner;
import io.github.cuiyunlong.llmguard.core.exec.TimeoutRunner;
import io.github.cuiyunlong.llmguard.core.fallback.FallbackRegistry;
import io.github.cuiyunlong.llmguard.core.lock.LockManager;
import io.github.cuiyunlong.llmguard.core.merge.MergeResult;
import io.github.cuiyunlong.llmguard.core.merge.MergeWindow;
import io.github.cuiyunlong.llmguard.core.observability.GuardLogger;
import io.github.cuiyunlong.llmguard.core.singleflight.Singleflight;
import io.github.cuiyunlong.llmguard.core.singleflight.SingleflightResult;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;

public class DefaultGuardExecutor implements GuardExecutor {
  private final LockManager lockManager;
  private final Singleflight singleflight;
  private final MergeWindow mergeWindow;
  private final TimeoutRunner timeoutRunner;
  private final RetryRunner retryRunner;
  private final FallbackRegistry fallbackRegistry;
  private final GuardLogger guardLogger;

  public DefaultGuardExecutor(
      LockManager lockManager,
      Singleflight singleflight,
      MergeWindow mergeWindow,
      TimeoutRunner timeoutRunner,
      RetryRunner retryRunner,
      FallbackRegistry fallbackRegistry,
      GuardLogger guardLogger) {
    this.lockManager = lockManager;
    this.singleflight = singleflight;
    this.mergeWindow = mergeWindow;
    this.timeoutRunner = timeoutRunner;
    this.retryRunner = retryRunner;
    this.fallbackRegistry = fallbackRegistry;
    this.guardLogger = guardLogger;
  }

  @Override
  public <T> T execute(CallContext ctx, EffectivePlan plan, Callable<T> supplier) throws Exception {
    GuardResultMeta meta = new GuardResultMeta();
    long startAtMs = System.currentTimeMillis();

    try {
      Callable<T> call = () -> retryRunner.run(
          () -> timeoutRunner.run(supplier, plan.getTimeoutMs()),
          plan.getRetries(),
          plan.getBackoffMs());

      if (plan.getMergeWindowMs() > 0) {
        Callable<T> merged = call;
        call = () -> {
          MergeResult<T> result = mergeWindow.merge(ctx.getKey(), plan.getMergeWindowMs(), merged);
          meta.setMerged(result.isMerged());
          return result.getResult();
        };
      }

      if (plan.isSingleflightEnabled()) {
        Callable<T> singleflightCall = call;
        call = () -> {
          SingleflightResult<T> result = singleflight.execute(ctx.getKey(), singleflightCall);
          meta.setSingleflightHit(result.isHit());
          return result.getResult();
        };
      }

      if (plan.isLockEnabled()) {
        Callable<T> locked = call;
        call = () -> lockManager.withLock(ctx.getKey(), plan.getLockTtlMs(), locked);
      }

      T result = call.call();
      meta.setStatus(GuardStatus.SUCCESS);
      return result;
    } catch (Throwable t) {
      GuardStatus status = (t instanceof TimeoutException) ? GuardStatus.TIMEOUT : GuardStatus.ERROR;
      meta.setStatus(status);

      FallbackHandler handler = fallbackRegistry.get(plan.getFallbackBean());
      if (handler != null) {
        meta.setFallbackUsed(true);
        meta.setStatus(GuardStatus.FALLBACK);
        return (T) handler.fallback(ctx, t);
      }

      if (t instanceof Exception) {
        throw (Exception) t;
      }
      throw new Exception(t);
    } finally {
      long latencyMs = System.currentTimeMillis() - startAtMs;
      guardLogger.log(ctx, plan, meta, latencyMs);
    }
  }
}
