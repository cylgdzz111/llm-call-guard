package io.github.cuiyunlong.llmguard.core.lock;

import java.util.concurrent.Callable;

public interface LockManager {
  <T> T withLock(String key, long ttlMs, Callable<T> supplier) throws Exception;
}
