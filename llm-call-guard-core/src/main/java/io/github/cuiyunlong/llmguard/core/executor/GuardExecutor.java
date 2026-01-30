package io.github.cuiyunlong.llmguard.core.executor;

import io.github.cuiyunlong.llmguard.api.model.CallContext;
import io.github.cuiyunlong.llmguard.api.model.EffectivePlan;
import java.util.concurrent.Callable;

public interface GuardExecutor {
  <T> T execute(CallContext ctx, EffectivePlan plan, Callable<T> supplier) throws Exception;
}
