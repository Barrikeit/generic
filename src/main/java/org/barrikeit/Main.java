package org.barrikeit;

import org.barrikeit.webapp.ContainerConfiguration;
import org.barrikeit.webapp.ContainerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

  public static void main(String[] args) {
    AnnotationConfigApplicationContext springContext =
        new AnnotationConfigApplicationContext(ContainerConfiguration.class);

    ContainerFactory containerFactory = springContext.getBean(ContainerFactory.class);
    containerFactory.start();
  }
}
