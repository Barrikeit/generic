package org.barrikeit.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@EnableScheduling
public class ServerStatus {

  @Scheduled(initialDelay = 60, fixedDelay = 60, timeUnit = TimeUnit.SECONDS)
  public void task() {
    String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
    log.info("Server Status - [UP]: [{}]", currentTime);
  }
}
