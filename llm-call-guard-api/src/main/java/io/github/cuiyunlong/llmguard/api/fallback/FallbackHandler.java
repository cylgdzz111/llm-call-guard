package io.github.cuiyunlong.llmguard.api.fallback;

import io.github.cuiyunlong.llmguard.api.model.CallContext;

public interface FallbackHandler {
  Object fallback(CallContext ctx, Throwable cause);
}
