package io.github.cuiyunlong.llmguard.starter.config;

import io.github.cuiyunlong.llmguard.api.annotation.LLMGuard;
import io.github.cuiyunlong.llmguard.api.fallback.FallbackHandler;
import io.github.cuiyunlong.llmguard.api.spi.KeyResolver;
import io.github.cuiyunlong.llmguard.api.spi.TagExtractor;
import io.github.cuiyunlong.llmguard.core.exec.DefaultRetryRunner;
import io.github.cuiyunlong.llmguard.core.exec.DefaultTimeoutRunner;
import io.github.cuiyunlong.llmguard.core.exec.RetryRunner;
import io.github.cuiyunlong.llmguard.core.exec.TimeoutRunner;
import io.github.cuiyunlong.llmguard.core.executor.DefaultGuardExecutor;
import io.github.cuiyunlong.llmguard.core.executor.GuardExecutor;
import io.github.cuiyunlong.llmguard.core.fallback.DefaultFallbackRegistry;
import io.github.cuiyunlong.llmguard.core.fallback.FallbackRegistry;
import io.github.cuiyunlong.llmguard.core.key.DefaultKeyResolver;
import io.github.cuiyunlong.llmguard.core.lock.LocalLockManager;
import io.github.cuiyunlong.llmguard.core.lock.LockManager;
import io.github.cuiyunlong.llmguard.core.merge.DelayMergeWindow;
import io.github.cuiyunlong.llmguard.core.merge.MergeWindow;
import io.github.cuiyunlong.llmguard.core.observability.DefaultGuardLogger;
import io.github.cuiyunlong.llmguard.core.observability.GuardLogger;
import io.github.cuiyunlong.llmguard.core.singleflight.DefaultSingleflight;
import io.github.cuiyunlong.llmguard.core.singleflight.Singleflight;
import io.github.cuiyunlong.llmguard.starter.aop.LLMGuardAspect;
import io.github.cuiyunlong.llmguard.starter.aop.PlanFactory;
import io.github.cuiyunlong.llmguard.starter.spel.SpelKeyResolver;
import io.github.cuiyunlong.llmguard.starter.tags.DefaultTagExtractor;
import java.util.Map;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(LLMGuard.class)
@EnableConfigurationProperties(LLMGuardProperties.class)
@ConditionalOnProperty(prefix = "llm.guard", name = "enabled", havingValue = "true", matchIfMissing = true)
public class LLMGuardAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public PlanFactory planFactory(LLMGuardProperties properties) {
    return new PlanFactory(properties);
  }

  @Bean
  @ConditionalOnMissingBean
  public SpelKeyResolver spelKeyResolver() {
    return new SpelKeyResolver();
  }

  @Bean
  @ConditionalOnMissingBean
  public TagExtractor tagExtractor() {
    return new DefaultTagExtractor();
  }

  @Bean
  @ConditionalOnMissingBean
  public KeyResolver keyResolver() {
    return new DefaultKeyResolver();
  }

  @Bean
  @ConditionalOnMissingBean
  public LockManager lockManager() {
    return new LocalLockManager();
  }

  @Bean
  @ConditionalOnMissingBean
  public Singleflight singleflight() {
    return new DefaultSingleflight();
  }

  @Bean
  @ConditionalOnMissingBean
  public MergeWindow mergeWindow() {
    return new DelayMergeWindow();
  }

  @Bean
  @ConditionalOnMissingBean
  public TimeoutRunner timeoutRunner() {
    return new DefaultTimeoutRunner();
  }

  @Bean
  @ConditionalOnMissingBean
  public RetryRunner retryRunner() {
    return new DefaultRetryRunner();
  }

  @Bean
  @ConditionalOnMissingBean
  public FallbackRegistry fallbackRegistry(Map<String, FallbackHandler> handlers) {
    return new DefaultFallbackRegistry(handlers);
  }

  @Bean
  @ConditionalOnMissingBean
  public GuardLogger guardLogger(LLMGuardProperties properties) {
    if (!properties.getObservability().isLogEnabled()) {
      return (ctx, plan, meta, latencyMs) -> {
      };
    }
    return new DefaultGuardLogger();
  }

  @Bean
  @ConditionalOnMissingBean
  public GuardExecutor guardExecutor(
      LockManager lockManager,
      Singleflight singleflight,
      MergeWindow mergeWindow,
      TimeoutRunner timeoutRunner,
      RetryRunner retryRunner,
      FallbackRegistry fallbackRegistry,
      GuardLogger guardLogger) {
    return new DefaultGuardExecutor(
        lockManager,
        singleflight,
        mergeWindow,
        timeoutRunner,
        retryRunner,
        fallbackRegistry,
        guardLogger);
  }

  @Bean
  @ConditionalOnMissingBean
  public LLMGuardAspect llmGuardAspect(
      GuardExecutor guardExecutor,
      KeyResolver keyResolver,
      TagExtractor tagExtractor,
      PlanFactory planFactory,
      SpelKeyResolver spelKeyResolver,
      LLMGuardProperties properties) {
    return new LLMGuardAspect(
        guardExecutor,
        keyResolver,
        tagExtractor,
        planFactory,
        spelKeyResolver,
        properties);
  }
}
