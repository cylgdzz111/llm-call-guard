package io.github.cuiyunlong.llmguard.demo;

import io.github.cuiyunlong.llmguard.api.model.CallContext;
import io.github.cuiyunlong.llmguard.api.model.EffectivePlan;
import io.github.cuiyunlong.llmguard.api.model.GuardResultMeta;
import io.github.cuiyunlong.llmguard.core.observability.GuardLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DemoGuardLogger implements GuardLogger {
  
  @Override
  public void log(CallContext ctx, EffectivePlan plan, GuardResultMeta meta, long latencyMs) {
    log.info("guard traceId={} scene={} key={} status={} latencyMs={} singleflight={} merged={} fallback={} ",
        ctx.getTraceId(),
        ctx.getScene(),
        ctx.getKey(),
        meta.getStatus(),
        latencyMs,
        meta.isSingleflightHit(),
        meta.isMerged(),
        meta.isFallbackUsed());
  }
}
