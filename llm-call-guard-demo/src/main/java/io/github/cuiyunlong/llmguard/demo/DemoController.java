package io.github.cuiyunlong.llmguard.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
  private final TicketAiService ticketAiService;

  public DemoController(TicketAiService ticketAiService) {
    this.ticketAiService = ticketAiService;
  }

  @GetMapping("/demo")
  public String demo(@RequestParam("sessionId") String sessionId) throws Exception {
    return ticketAiService.extract(sessionId);
  }
}
