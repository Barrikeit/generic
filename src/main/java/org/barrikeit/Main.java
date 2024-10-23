package org.barrikeit;

import lombok.extern.log4j.Log4j2;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.barrikeit.config.ApplicationConfiguration;
import org.barrikeit.config.ServletContainerFactory;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

@Log4j2
public class Main {

  public static void main(String[] args) throws LifecycleException {
    AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
    context.register(ApplicationConfiguration.class);
    context.refresh();
    ServletContainerFactory servletContainerFactory = context.getBean(ServletContainerFactory.class);
    Tomcat tomcat = servletContainerFactory.tomcatFactory();

    try {
      tomcat.start();
      log.info("Tomcat started on port {}", tomcat.getConnector().getLocalPort());
      tomcat.getServer().await(); // Wait for requests
    } catch (LifecycleException e) {
      log.error("Failed to start Tomcat", e);
    }
  }
}
