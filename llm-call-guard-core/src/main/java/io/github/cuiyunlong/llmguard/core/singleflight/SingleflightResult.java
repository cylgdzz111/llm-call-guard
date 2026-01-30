package io.github.cuiyunlong.llmguard.core.singleflight;

public class SingleflightResult<T> {
  private final T result;
  private final boolean hit;

  public SingleflightResult(T result, boolean hit) {
    this.result = result;
    this.hit = hit;
  }

  public T getResult() {
    return result;
  }

  public boolean isHit() {
    return hit;
  }
}
