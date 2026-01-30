package io.github.cuiyunlong.llmguard.api.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CallContext {
  private final String traceId;
  private final String key;
  private final String scene;
  private final Map<String, String> tags;
  private final long startAtMs;

  public CallContext(String traceId, String key, String scene, Map<String, String> tags, long startAtMs) {
    this.traceId = traceId;
    this.key = key;
    this.scene = scene;
    this.tags = tags == null ? Collections.emptyMap() : Collections.unmodifiableMap(new HashMap<>(tags));
    this.startAtMs = startAtMs;
  }

  public String getTraceId() {
    return traceId;
  }

  public String getKey() {
    return key;
  }

  public String getScene() {
    return scene;
  }

  public Map<String, String> getTags() {
    return tags;
  }

  public long getStartAtMs() {
    return startAtMs;
  }
}
