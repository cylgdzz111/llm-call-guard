package io.github.cuiyunlong.llmguard.core.exec;

import java.util.concurrent.Callable;

public interface RetryRunner {
  <T> T run(Callable<T> supplier, int retries, long backoffMs) throws Exception;
}
