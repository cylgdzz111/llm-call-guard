package io.github.cuiyunlong.llmguard.core.exec;

import java.util.concurrent.Callable;

public interface TimeoutRunner {
  <T> T run(Callable<T> supplier, long timeoutMs) throws Exception;
}
