package org.barrikeit.webapp;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@AllArgsConstructor
public class WebAppContextListener implements ServletContextListener {

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    // Initialize the Spring context and set it in the servlet context
    log.info("*-* Spring context initialized.");
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    // Close the Spring context when the application is stopped
    log.info("*-* Spring context destroyed.");
  }
}
