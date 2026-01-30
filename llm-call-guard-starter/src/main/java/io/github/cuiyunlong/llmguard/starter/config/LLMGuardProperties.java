package io.github.cuiyunlong.llmguard.starter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "llm.guard")
public class LLMGuardProperties {
  private boolean enabled = true;
  private Defaults defaults = new Defaults();
  private Observability observability = new Observability();

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public Defaults getDefaults() {
    return defaults;
  }

  public void setDefaults(Defaults defaults) {
    this.defaults = defaults;
  }

  public Observability getObservability() {
    return observability;
  }

  public void setObservability(Observability observability) {
    this.observability = observability;
  }

  public static class Defaults {
    private long timeoutMs = 8000;
    private int retries = 0;
    private long backoffMs = 200;
    private long mergeWindowMs = 0;
    private boolean lockEnabled = true;
    private long lockTtlMs = 15000;
    private boolean singleflightEnabled = true;

    public long getTimeoutMs() {
      return timeoutMs;
    }

    public void setTimeoutMs(long timeoutMs) {
      this.timeoutMs = timeoutMs;
    }

    public int getRetries() {
      return retries;
    }

    public void setRetries(int retries) {
      this.retries = retries;
    }

    public long getBackoffMs() {
      return backoffMs;
    }

    public void setBackoffMs(long backoffMs) {
      this.backoffMs = backoffMs;
    }

    public long getMergeWindowMs() {
      return mergeWindowMs;
    }

    public void setMergeWindowMs(long mergeWindowMs) {
      this.mergeWindowMs = mergeWindowMs;
    }

    public boolean isLockEnabled() {
      return lockEnabled;
    }

    public void setLockEnabled(boolean lockEnabled) {
      this.lockEnabled = lockEnabled;
    }

    public long getLockTtlMs() {
      return lockTtlMs;
    }

    public void setLockTtlMs(long lockTtlMs) {
      this.lockTtlMs = lockTtlMs;
    }

    public boolean isSingleflightEnabled() {
      return singleflightEnabled;
    }

    public void setSingleflightEnabled(boolean singleflightEnabled) {
      this.singleflightEnabled = singleflightEnabled;
    }
  }

  public static class Observability {
    private boolean logEnabled = true;
    private boolean includeArgs = false;
    private boolean includeResult = false;

    public boolean isLogEnabled() {
      return logEnabled;
    }

    public void setLogEnabled(boolean logEnabled) {
      this.logEnabled = logEnabled;
    }

    public boolean isIncludeArgs() {
      return includeArgs;
    }

    public void setIncludeArgs(boolean includeArgs) {
      this.includeArgs = includeArgs;
    }

    public boolean isIncludeResult() {
      return includeResult;
    }

    public void setIncludeResult(boolean includeResult) {
      this.includeResult = includeResult;
    }
  }
}
