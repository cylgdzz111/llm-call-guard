package io.github.cuiyunlong.llmguard.core.merge;

import java.util.concurrent.Callable;

public interface MergeWindow {
  <T> MergeResult<T> merge(String key, long windowMs, Callable<T> supplier) throws Exception;
}
