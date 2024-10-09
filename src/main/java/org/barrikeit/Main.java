package org.barrikeit;

import lombok.extern.log4j.Log4j2;
import org.barrikeit.config.ApplicationConfiguration;
import org.barrikeit.config.DBConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@Log4j2
public class Main {

  public static void main(String[] args) {
    log.info("Loading application context...");
    ApplicationContext context =
        new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
    DBConfiguration config = (DBConfiguration) context.getBean("DBConfiguration");
    log.info(
        "Wellcome to this app\nThis is your database info:\n-Path: {}\n-Username: {}\n-Password: {}\n",
        config.getPath(),
        config.getUsername(),
        config.getPassword());
  }
}
