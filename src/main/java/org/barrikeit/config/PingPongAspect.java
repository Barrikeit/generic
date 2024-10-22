package org.barrikeit.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.log4j.Log4j2;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class PingPongAspect implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        // Before executing the target method
        log.info("Ping-Pong: Invoking method {}", invocation.getMethod().getName());
        return invocation.proceed(); // Proceed to the target method
    }

    // Scheduled method to log every 30 seconds
    @Scheduled(fixedRate = 30000)
    public void logPingPong() {
        log.info("{} Ping-Pong", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
    }

}