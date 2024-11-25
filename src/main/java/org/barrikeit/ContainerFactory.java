package org.barrikeit;

import static org.barrikeit.util.FileUtil.createTempFolder;

import java.io.File;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.barrikeit.config.WebMvcConfig;
import org.barrikeit.controller.UserController;
import org.barrikeit.application.ApplicationProperties;
import org.barrikeit.service.dto.UserDto;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@Log4j2
public class ContainerFactory {

  private static AnnotationConfigWebApplicationContext applicationContext;

  private ContainerFactory() {}

  public static void start(AnnotationConfigApplicationContext mainContext) {
    Tomcat tomcat = embeddedTomcat(mainContext);
    startTomcat(tomcat);
  }

  private static Tomcat embeddedTomcat(AnnotationConfigApplicationContext mainContext) {
    // Get the serverProperties from the main context loaded from the application files
    ApplicationProperties.ServerProperties serverProperties =
        mainContext.getBean(ApplicationProperties.ServerProperties.class);
    // Configure the tomcat
    int port = serverProperties.getPort();
    File baseDir = createTempFolder("embedded-tomcat", port);
    Tomcat tomcat = new Tomcat();
    tomcat.setPort(port);
    tomcat.setSilent(true);
    tomcat.setAddDefaultWebXmlToWebapp(false);
    tomcat.setBaseDir(baseDir.getAbsolutePath());
    tomcat.getConnector();

    // Provide a proper base directory
    Context rootContext =
        tomcat.addContext(serverProperties.getContextPath(), baseDir.getAbsolutePath());

    // Create the Web Application Context
    applicationContext = new AnnotationConfigWebApplicationContext();
    applicationContext.setParent(mainContext);
    applicationContext.register(WebMvcConfig.class);
    applicationContext.setServletContext(rootContext.getServletContext());
    applicationContext.refresh();

    // Set up the DispatcherServlet
    DispatcherServlet dispatcherServlet = new DispatcherServlet(applicationContext);
    Tomcat.addServlet(rootContext, serverProperties.getName(), dispatcherServlet);
    rootContext.addServletMappingDecoded("/", serverProperties.getName());
    rootContext.addServletMappingDecoded("/api", serverProperties.getName());
    return tomcat;
  }

  private static void startTomcat(Tomcat tomcat) {
    try {
      tomcat.start();
      tomcat.getServer().await();

      String url = tomcat.getHost().getName() + ":" + tomcat.getConnector().getLocalPort();
      log.info("Application started on {}", url);
      UserController userController = applicationContext.getBean(UserController.class);
      userController.save(UserDto.builder().username("username").email("mail@generic.es").build());

    } catch (LifecycleException e) {
      log.error("Failed to start Application", e);
    }
  }
}
