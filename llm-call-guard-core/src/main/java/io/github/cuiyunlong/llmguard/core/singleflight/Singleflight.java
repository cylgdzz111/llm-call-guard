package io.github.cuiyunlong.llmguard.core.singleflight;

import java.util.concurrent.Callable;

public interface Singleflight {
  <T> SingleflightResult<T> execute(String key, Callable<T> supplier) throws Exception;
}
