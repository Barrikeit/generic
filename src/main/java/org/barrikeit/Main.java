package org.barrikeit;

import lombok.extern.log4j.Log4j2;
import org.barrikeit.application.ApplicationConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Log4j2
public class Main {

  public static void main(String[] args) {
    AnnotationConfigApplicationContext mainContext =
        new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
    log.debug("Main Context started");
    ContainerFactory.start(mainContext);
  }
}
