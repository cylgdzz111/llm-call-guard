package io.github.cuiyunlong.llmguard.core.fallback;

import io.github.cuiyunlong.llmguard.api.fallback.FallbackHandler;

public interface FallbackRegistry {
  FallbackHandler get(String name);
}
