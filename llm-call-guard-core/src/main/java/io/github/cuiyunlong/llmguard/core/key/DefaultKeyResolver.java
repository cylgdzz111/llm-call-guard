package io.github.cuiyunlong.llmguard.core.key;

import io.github.cuiyunlong.llmguard.api.spi.KeyResolver;
import java.lang.reflect.Method;
import java.util.Map;

public class DefaultKeyResolver implements KeyResolver {
  private static final String PREFIX = "llmguard";

  @Override
  public String resolve(String scene, Method method, Object[] args, Map<String, String> tags) {
    String tenantId = value(tags, "tenantId");
    String userId = value(tags, "userId");
    String sessionId = value(tags, "sessionId");

    StringBuilder sb = new StringBuilder();
    sb.append(PREFIX).append(":").append(scene);

    if (notBlank(tenantId)) {
      sb.append(":t:").append(tenantId);
    }
    if (notBlank(userId)) {
      sb.append(":u:").append(userId);
    }
    if (notBlank(sessionId)) {
      sb.append(":s:").append(sessionId);
    }

    if (!notBlank(tenantId) && !notBlank(userId) && !notBlank(sessionId)) {
      sb.append(":").append(method.getDeclaringClass().getName())
          .append("#").append(method.getName());
    }

    return sb.toString();
  }

  private String value(Map<String, String> tags, String key) {
    return tags == null ? null : tags.get(key);
  }

  private boolean notBlank(String value) {
    return value != null && !value.trim().isEmpty();
  }
}
