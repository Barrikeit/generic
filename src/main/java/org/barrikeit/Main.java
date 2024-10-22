package org.barrikeit;

import lombok.extern.log4j.Log4j2;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.barrikeit.rest.UserController;
import org.barrikeit.service.dto.UserDto;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@Log4j2
public class Main {

  public static void main(String[] args) throws LifecycleException {
    log.info("Loading application context...");

    // Initialize the web application context but don't refresh yet
    AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();

    // Create a Tomcat instance
    Tomcat tomcat = new Tomcat();
    tomcat.setPort(8080);

    // Create a new context for Tomcat
    String classPath = System.getProperty("user.dir") + "/target/classes";
    Context context = tomcat.addWebapp("", classPath);

    // Set the ServletContext in Spring's context before refresh
    rootContext.setServletContext(context.getServletContext());
    rootContext.refresh();

    // Start the Tomcat server
    tomcat.start();
    log.info("Tomcat started on port 8080");

    // Add some test users via UserController after context is ready
    WebApplicationContext webApplicationContext =
        WebApplicationContextUtils.getWebApplicationContext(context.getServletContext());
    UserController userRest = webApplicationContext.getBean(UserController.class);
//    userRest.save(UserDto.builder().username("alb").email("alb@mail.com").build());
//    userRest.save(UserDto.builder().username("bla").email("bla@mail.com").build());
//    userRest.save(UserDto.builder().username("abl").email("abl@mail.com").build());
//    userRest.save(UserDto.builder().username("lba").email("lba@mail.com").build());
//    userRest.save(UserDto.builder().username("bal").email("bal@mail.com").build());
//    userRest.save(UserDto.builder().username("lab").email("lab@mail.com").build());

    // Keep the server running
    tomcat.getServer().await();
  }
}
