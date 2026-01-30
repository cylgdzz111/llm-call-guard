package io.github.cuiyunlong.llmguard.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class DemoController {
  private final TicketAiService ticketAiService;

  public DemoController(TicketAiService ticketAiService) {
    this.ticketAiService = ticketAiService;
  }

  @GetMapping("/demo")
  public String demo(@RequestParam("sessionId") String sessionId) throws Exception {
    log.info("demo request: sessionId={}", sessionId);
    return ticketAiService.extract(sessionId);
  }
}
