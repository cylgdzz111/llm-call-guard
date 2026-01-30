package io.github.cuiyunlong.llmguard.demo;

import io.github.cuiyunlong.llmguard.api.annotation.LLMGuard;
import org.springframework.stereotype.Service;

@Service
public class TicketAiService {

  @LLMGuard(scene = "ticket_autofill", timeoutMs = 2000, retries = 0, fallback = "ticketFallback")
  public String extract(String sessionId) throws InterruptedException {
    Thread.sleep(200);
    return "ok:" + sessionId;
  }
}
