package io.github.cuiyunlong.llmguard.starter.spel;

import java.lang.reflect.Method;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.expression.spel.support.StandardTypeLocator;

public class SpelKeyResolver {
  private final ExpressionParser parser = new SpelExpressionParser();
  private final DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

  public String resolve(String expression, Method method, Object[] args) {
    StandardEvaluationContext context = new StandardEvaluationContext();
    String[] paramNames = nameDiscoverer.getParameterNames(method);
    if (paramNames != null && args != null) {
      for (int i = 0; i < paramNames.length && i < args.length; i++) {
        context.setVariable(paramNames[i], args[i]);
      }
    }
    context.setTypeLocator(new StandardTypeLocator(method.getDeclaringClass().getClassLoader()));
    Object value = parser.parseExpression(expression).getValue(context);
    return value == null ? null : String.valueOf(value);
  }
}
