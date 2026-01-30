package io.github.cuiyunlong.llmguard.starter.aop;

import io.github.cuiyunlong.llmguard.api.annotation.LLMGuard;
import io.github.cuiyunlong.llmguard.api.model.CallContext;
import io.github.cuiyunlong.llmguard.api.model.EffectivePlan;
import io.github.cuiyunlong.llmguard.api.spi.KeyResolver;
import io.github.cuiyunlong.llmguard.api.spi.TagExtractor;
import io.github.cuiyunlong.llmguard.core.executor.GuardExecutor;
import io.github.cuiyunlong.llmguard.starter.config.LLMGuardProperties;
import io.github.cuiyunlong.llmguard.starter.spel.SpelKeyResolver;
import io.github.cuiyunlong.llmguard.starter.util.TraceIdGenerator;
import java.lang.reflect.Method;
import java.util.Map;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class LLMGuardAspect {
  private final GuardExecutor guardExecutor;
  private final KeyResolver keyResolver;
  private final TagExtractor tagExtractor;
  private final PlanFactory planFactory;
  private final SpelKeyResolver spelKeyResolver;
  private final LLMGuardProperties properties;

  public LLMGuardAspect(
      GuardExecutor guardExecutor,
      KeyResolver keyResolver,
      TagExtractor tagExtractor,
      PlanFactory planFactory,
      SpelKeyResolver spelKeyResolver,
      LLMGuardProperties properties) {
    this.guardExecutor = guardExecutor;
    this.keyResolver = keyResolver;
    this.tagExtractor = tagExtractor;
    this.planFactory = planFactory;
    this.spelKeyResolver = spelKeyResolver;
    this.properties = properties;
  }

  @Around("@annotation(guard)")
  public Object around(ProceedingJoinPoint pjp, LLMGuard guard) throws Throwable {
    if (!properties.isEnabled()) {
      return pjp.proceed();
    }

    Method method = ((MethodSignature) pjp.getSignature()).getMethod();
    Object[] args = pjp.getArgs();

    Map<String, String> tags = tagExtractor.extract(method, args);
    String key = resolveKey(guard, method, args, tags);

    CallContext ctx = new CallContext(
        TraceIdGenerator.nextId(),
        key,
        guard.scene(),
        tags,
        System.currentTimeMillis());

    EffectivePlan plan = planFactory.build(guard);

    return guardExecutor.execute(ctx, plan, () -> {
      try {
        return pjp.proceed();
      } catch (Throwable t) {
        if (t instanceof Exception) {
          throw (Exception) t;
        }
        throw new Exception(t);
      }
    });
  }

  private String resolveKey(LLMGuard guard, Method method, Object[] args, Map<String, String> tags) {
    String keyExpression = guard.key();
    if (keyExpression != null && !keyExpression.trim().isEmpty()) {
      String resolved = spelKeyResolver.resolve(keyExpression, method, args);
      if (resolved != null && !resolved.trim().isEmpty()) {
        return resolved;
      }
    }
    return keyResolver.resolve(guard.scene(), method, args, tags);
  }
}
