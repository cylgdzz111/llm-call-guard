package io.github.cuiyunlong.llmguard.starter.util;

import java.util.UUID;

public final class TraceIdGenerator {
  private TraceIdGenerator() {
  }

  public static String nextId() {
    return UUID.randomUUID().toString().replace("-", "");
  }
}
