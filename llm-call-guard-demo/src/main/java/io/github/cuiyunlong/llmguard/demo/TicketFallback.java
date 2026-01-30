package io.github.cuiyunlong.llmguard.demo;

import io.github.cuiyunlong.llmguard.api.fallback.FallbackHandler;
import io.github.cuiyunlong.llmguard.api.model.CallContext;
import org.springframework.stereotype.Component;

@Component("ticketFallback")
public class TicketFallback implements FallbackHandler {
  @Override
  public Object fallback(CallContext ctx, Throwable cause) {
    return "fallback:" + ctx.getScene();
  }
}
