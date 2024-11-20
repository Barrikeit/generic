package org.barrikeit;

import lombok.extern.log4j.Log4j2;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.barrikeit.config.ApplicationConfiguration;
import org.barrikeit.webapp.ContainerConfiguration;
import org.barrikeit.webapp.ContainerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Log4j2
public class Main {

  public static void main(String[] args) {
    AnnotationConfigApplicationContext springContext =
        new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
    AnnotationConfigApplicationContext containerContext = new AnnotationConfigApplicationContext();
    containerContext.setParent(springContext);
    containerContext.register(ContainerConfiguration.class);
    containerContext.refresh();

    start(containerContext);
  }

  public static void start(AnnotationConfigApplicationContext containerContext) {
    try {
      ContainerFactory containerFactory = containerContext.getBean(ContainerFactory.class);
      Tomcat tomcat = containerFactory.embeddedTomcat();
      tomcat.start();
      String url = tomcat.getHost().getName() + ":" + tomcat.getConnector().getLocalPort();
      log.info("Application started on {}", url);

      // UserController userController = context.getBean(UserController.class);
      // userController.save(UserDto.builder().username("username").email("email@e.es").build());

      tomcat.getServer().await();
    } catch (LifecycleException e) {
      log.error("Failed to start Application", e);
    }
  }
}
