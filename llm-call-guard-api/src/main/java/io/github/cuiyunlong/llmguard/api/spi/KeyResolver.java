package io.github.cuiyunlong.llmguard.api.spi;

import java.lang.reflect.Method;
import java.util.Map;

public interface KeyResolver {
  String resolve(String scene, Method method, Object[] args, Map<String, String> tags);
}
