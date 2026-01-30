package io.github.cuiyunlong.llmguard.starter.tags;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public final class TagExtractors {
  private TagExtractors() {
  }

  public static String readString(Object target, String name) {
    if (target == null || name == null) {
      return null;
    }
    if (target instanceof Map) {
      Object value = ((Map<?, ?>) target).get(name);
      return value == null ? null : String.valueOf(value);
    }

    String getter = "get" + capitalize(name);
    try {
      Method method = target.getClass().getMethod(getter);
      Object value = method.invoke(target);
      return value == null ? null : String.valueOf(value);
    } catch (Exception ignored) {
    }

    try {
      Field field = target.getClass().getDeclaredField(name);
      field.setAccessible(true);
      Object value = field.get(target);
      return value == null ? null : String.valueOf(value);
    } catch (Exception ignored) {
      return null;
    }
  }

  private static String capitalize(String value) {
    if (value == null || value.isEmpty()) {
      return value;
    }
    return Character.toUpperCase(value.charAt(0)) + value.substring(1);
  }
}
