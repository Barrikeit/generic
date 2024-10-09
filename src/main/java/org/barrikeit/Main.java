package org.barrikeit;

import java.util.List;
import java.util.Objects;
import lombok.extern.log4j.Log4j2;
import org.barrikeit.config.ApplicationConfiguration;
import org.barrikeit.config.DatabaseProperties;
import org.barrikeit.rest.UserController;
import org.barrikeit.service.dto.UserDto;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.ResponseEntity;

@Log4j2
public class Main {

  public static void main(String[] args) {
    log.info("Loading application context...");
    ApplicationContext context =
        new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
    DatabaseProperties config = context.getBean(DatabaseProperties.class);
    log.info(
        "\nWellcome to this app, this is your database info:\n\t-Path: {}\n\t-Username: {}\n\t-Password: {}\n",
        config.getUrl(),
        config.getUsername(),
        config.getPassword());

    UserController userController = context.getBean(UserController.class);
    userController.save(UserDto.builder().username("prueb1").email("prueba1@gmail.com").build());
    userController.save(UserDto.builder().username("prueba2").email("prueba2@gmail.com").build());
    ResponseEntity<List<UserDto>> responseEntity = userController.findAll();
    Objects.requireNonNull(responseEntity.getBody())
        .forEach(userDto -> log.info(userDto.toString()));
  }
}
