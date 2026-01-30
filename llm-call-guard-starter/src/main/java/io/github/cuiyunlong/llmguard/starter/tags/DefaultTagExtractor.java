package io.github.cuiyunlong.llmguard.starter.tags;

import io.github.cuiyunlong.llmguard.api.spi.TagExtractor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class DefaultTagExtractor implements TagExtractor {
  @Override
  public Map<String, String> extract(Method method, Object[] args) {
    Map<String, String> tags = new HashMap<>();
    if (args == null) {
      return tags;
    }

    for (Object arg : args) {
      if (arg == null) {
        continue;
      }
      putIfPresent(tags, "tenantId", TagExtractors.readString(arg, "tenantId"));
      putIfPresent(tags, "userId", TagExtractors.readString(arg, "userId"));
      putIfPresent(tags, "sessionId", TagExtractors.readString(arg, "sessionId"));
    }

    return tags;
  }

  private void putIfPresent(Map<String, String> tags, String key, String value) {
    if (value != null && !value.trim().isEmpty() && !tags.containsKey(key)) {
      tags.put(key, value);
    }
  }
}
