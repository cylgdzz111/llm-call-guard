package io.github.cuiyunlong.llmguard.core.observability;

import io.github.cuiyunlong.llmguard.api.model.CallContext;
import io.github.cuiyunlong.llmguard.api.model.EffectivePlan;
import io.github.cuiyunlong.llmguard.api.model.GuardResultMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultGuardLogger implements GuardLogger {
  private static final Logger log = LoggerFactory.getLogger(DefaultGuardLogger.class);

  @Override
  public void log(CallContext ctx, EffectivePlan plan, GuardResultMeta meta, long latencyMs) {
    log.info("{\"traceId\":\"{}\",\"scene\":\"{}\",\"key\":\"{}\",\"latencyMs\":{},\"status\":\"{}\",\"singleflight\":{},\"merged\":{},\"fallback\":{}}",
        ctx.getTraceId(),
        ctx.getScene(),
        ctx.getKey(),
        latencyMs,
        meta.getStatus(),
        meta.isSingleflightHit(),
        meta.isMerged(),
        meta.isFallbackUsed());
  }
}
