package io.github.cuiyunlong.llmguard.api.model;

import io.github.cuiyunlong.llmguard.api.model.enums.GuardStatus;

public class GuardResultMeta {
  private GuardStatus status;
  private boolean singleflightHit;
  private boolean merged;
  private boolean fallbackUsed;

  public GuardStatus getStatus() {
    return status;
  }

  public void setStatus(GuardStatus status) {
    this.status = status;
  }

  public boolean isSingleflightHit() {
    return singleflightHit;
  }

  public void setSingleflightHit(boolean singleflightHit) {
    this.singleflightHit = singleflightHit;
  }

  public boolean isMerged() {
    return merged;
  }

  public void setMerged(boolean merged) {
    this.merged = merged;
  }

  public boolean isFallbackUsed() {
    return fallbackUsed;
  }

  public void setFallbackUsed(boolean fallbackUsed) {
    this.fallbackUsed = fallbackUsed;
  }
}
