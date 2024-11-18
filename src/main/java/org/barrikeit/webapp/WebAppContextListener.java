package org.barrikeit.webapp;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

@Log4j2
@Configuration
@AllArgsConstructor
public class WebAppContextListener implements ServletContextListener {

  private final AnnotationConfigWebApplicationContext context;

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    // Initialize the Spring context and set it in the servlet context
    ServletContext servletContext = sce.getServletContext();
    servletContext.setAttribute(
        WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, context);
    log.info("*-* Spring context initialized.");
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    // Close the Spring context when the application is stopped
    if (context != null) {
      context.close();
    }
    log.info("*-* Spring context destroyed.");
  }
}
