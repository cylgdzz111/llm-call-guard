package io.github.cuiyunlong.llmguard.core.exec;

import java.util.concurrent.Callable;

public class DefaultRetryRunner implements RetryRunner {
  @Override
  public <T> T run(Callable<T> supplier, int retries, long backoffMs) throws Exception {
    int attempts = 0;
    while (true) {
      try {
        return supplier.call();
      } catch (Throwable t) {
        if (attempts >= retries) {
          if (t instanceof Exception) {
            throw (Exception) t;
          }
          throw new Exception(t);
        }
        attempts++;
        if (backoffMs > 0) {
          try {
            Thread.sleep(backoffMs);
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw e;
          }
        }
      }
    }
  }
}
