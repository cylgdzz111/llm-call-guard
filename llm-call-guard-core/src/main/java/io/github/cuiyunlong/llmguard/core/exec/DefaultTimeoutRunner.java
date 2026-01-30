package io.github.cuiyunlong.llmguard.core.exec;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class DefaultTimeoutRunner implements TimeoutRunner {
  private final ExecutorService executor;

  public DefaultTimeoutRunner() {
    this(Executors.newCachedThreadPool(r -> {
      Thread t = new Thread(r, "llm-guard-timeout");
      t.setDaemon(true);
      return t;
    }));
  }

  public DefaultTimeoutRunner(ExecutorService executor) {
    this.executor = executor;
  }

  @Override
  public <T> T run(Callable<T> supplier, long timeoutMs) throws Exception {
    if (timeoutMs <= 0) {
      return supplier.call();
    }

    Future<T> future = executor.submit(supplier);
    try {
      return future.get(timeoutMs, TimeUnit.MILLISECONDS);
    } catch (TimeoutException e) {
      future.cancel(true);
      throw e;
    }
  }
}
