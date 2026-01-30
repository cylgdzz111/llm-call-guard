package io.github.cuiyunlong.llmguard.core.lock;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class LocalLockManager implements LockManager {
  private final ConcurrentHashMap<String, ReentrantLock> locks = new ConcurrentHashMap<>();

  @Override
  public <T> T withLock(String key, long ttlMs, Callable<T> supplier) throws Exception {
    ReentrantLock lock = locks.computeIfAbsent(key, k -> new ReentrantLock());
    lock.lock();
    try {
      return supplier.call();
    } finally {
      lock.unlock();
    }
  }
}
