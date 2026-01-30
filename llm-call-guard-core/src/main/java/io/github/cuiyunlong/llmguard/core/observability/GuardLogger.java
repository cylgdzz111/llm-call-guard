package io.github.cuiyunlong.llmguard.core.observability;

import io.github.cuiyunlong.llmguard.api.model.CallContext;
import io.github.cuiyunlong.llmguard.api.model.EffectivePlan;
import io.github.cuiyunlong.llmguard.api.model.GuardResultMeta;

public interface GuardLogger {
  void log(CallContext ctx, EffectivePlan plan, GuardResultMeta meta, long latencyMs);
}
