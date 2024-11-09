package org.barrikeit;

import lombok.extern.log4j.Log4j2;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.barrikeit.config.ApplicationConfiguration;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

@Log4j2
public class Main {

  public static void main(String[] args) {
    AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
    context.register(ApplicationConfiguration.class);
    context.refresh();

    Tomcat tomcat = context.getBean(Tomcat.class);

    try {
      System.setProperty("java.io.tmpdir", "dist/temp");
      tomcat.start();
      String url = tomcat.getHost().getName() + ":" + tomcat.getConnector().getLocalPort();
      log.info("Tomcat started on {}", url);

      // UserController userController = context.getBean(UserController.class);
      // userController.save(UserDto.builder().username("username").email("email@e.es").build());

      tomcat.getServer().await();
    } catch (LifecycleException e) {
      log.error("Failed to start Tomcat", e);
    }
  }
}
