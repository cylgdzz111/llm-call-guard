package io.github.cuiyunlong.llmguard.core.merge;

public class MergeResult<T> {
  private final T result;
  private final boolean merged;

  public MergeResult(T result, boolean merged) {
    this.result = result;
    this.merged = merged;
  }

  public T getResult() {
    return result;
  }

  public boolean isMerged() {
    return merged;
  }
}
