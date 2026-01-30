package io.github.cuiyunlong.llmguard.starter.aop;

import io.github.cuiyunlong.llmguard.api.annotation.LLMGuard;
import io.github.cuiyunlong.llmguard.api.model.EffectivePlan;
import io.github.cuiyunlong.llmguard.starter.config.LLMGuardProperties;

public class PlanFactory {
  private final LLMGuardProperties properties;

  public PlanFactory(LLMGuardProperties properties) {
    this.properties = properties;
  }

  public EffectivePlan build(LLMGuard guard) {
    EffectivePlan plan = new EffectivePlan();
    LLMGuardProperties.Defaults defaults = properties.getDefaults();

    plan.setLockEnabled(defaults.isLockEnabled());
    plan.setLockTtlMs(defaults.getLockTtlMs());
    plan.setSingleflightEnabled(defaults.isSingleflightEnabled());

    plan.setMergeWindowMs(resolveLong(guard.mergeWindowMs(), defaults.getMergeWindowMs()));
    plan.setTimeoutMs(resolveLong(guard.timeoutMs(), defaults.getTimeoutMs()));
    plan.setRetries(resolveInt(guard.retries(), defaults.getRetries()));
    plan.setBackoffMs(resolveLong(guard.backoffMs(), defaults.getBackoffMs()));

    plan.setFallbackBean(guard.fallback());
    return plan;
  }

  private long resolveLong(long annotated, long fallback) {
    return annotated >= 0 ? annotated : fallback;
  }

  private int resolveInt(int annotated, int fallback) {
    return annotated >= 0 ? annotated : fallback;
  }
}
