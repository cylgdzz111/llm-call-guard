package io.github.cuiyunlong.llmguard.api.spi;

import java.lang.reflect.Method;
import java.util.Map;

public interface TagExtractor {
  Map<String, String> extract(Method method, Object[] args);
}
