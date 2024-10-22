package org.barrikeit;

import lombok.extern.log4j.Log4j2;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

@Log4j2
public class Main {

  public static void main(String[] args) throws LifecycleException {
    log.info("Loading application context...");
    String userdir = System.getProperty("user.dir");

    // Initialize the web application context but don't refresh yet
    AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();

    // Create a Tomcat instance
    int port = 8080;
    Tomcat tomcat = new Tomcat();
    tomcat.setPort(port);
    tomcat.setSilent(true);
    tomcat.setBaseDir(userdir + "/dist/tomcat");

    // Create a new context for Tomcat
    Context context = tomcat.addWebapp("", userdir + "/target/classes");

    // Set the ServletContext in Spring's context before refresh
    rootContext.setServletContext(context.getServletContext());
    rootContext.refresh();

    // Start the Tomcat server
    tomcat.start();
    log.info("Tomcat started on port {}", port);

    // Uncomment the next line if you want to add a test user
    // WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(context.getServletContext());
    // UserController userRest = webApplicationContext.getBean(UserController.class);
    // userRest.save(UserDto.builder().username("alb").email("alb@mail.com").build());

    // Keep the server running
    tomcat.getServer().await();
  }
}
