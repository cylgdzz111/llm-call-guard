package io.github.cuiyunlong.llmguard.core.fallback;

import io.github.cuiyunlong.llmguard.api.fallback.FallbackHandler;
import java.util.Collections;
import java.util.Map;

public class DefaultFallbackRegistry implements FallbackRegistry {
  private final Map<String, FallbackHandler> handlers;

  public DefaultFallbackRegistry(Map<String, FallbackHandler> handlers) {
    this.handlers = handlers == null ? Collections.emptyMap() : handlers;
  }

  @Override
  public FallbackHandler get(String name) {
    if (name == null || name.trim().isEmpty()) {
      return null;
    }
    return handlers.get(name);
  }
}
