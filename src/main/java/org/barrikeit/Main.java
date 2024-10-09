package org.barrikeit;

import lombok.extern.log4j.Log4j2;
import org.barrikeit.config.ApplicationConfiguration;
import org.barrikeit.config.ApplicationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Log4j2
public class Main {

  public static void main(String[] args) {
    log.info("Loading application context...");
    ApplicationContext context =
        new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
    ApplicationProperties config = context.getBean(ApplicationProperties.class);
    log.info(
        "\nWellcome to this app, this is your database info:\n\t-Path: {}\n\t-Username: {}\n\t-Password: {}\n",
        config.getPath(),
        config.getUsername(),
        config.getPassword());
  }
}
