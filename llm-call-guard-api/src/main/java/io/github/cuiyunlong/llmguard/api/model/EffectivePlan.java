package io.github.cuiyunlong.llmguard.api.model;

public class EffectivePlan {
  private boolean lockEnabled;
  private long lockTtlMs;
  private boolean singleflightEnabled;
  private long mergeWindowMs;
  private long timeoutMs;
  private int retries;
  private long backoffMs;
  private String fallbackBean;

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

  public long getMergeWindowMs() {
    return mergeWindowMs;
  }

  public void setMergeWindowMs(long mergeWindowMs) {
    this.mergeWindowMs = mergeWindowMs;
  }

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

  public String getFallbackBean() {
    return fallbackBean;
  }

  public void setFallbackBean(String fallbackBean) {
    this.fallbackBean = fallbackBean;
  }
}
