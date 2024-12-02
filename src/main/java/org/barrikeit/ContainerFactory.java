package org.barrikeit;

import static org.barrikeit.util.FileUtil.createTempFolder;

import java.io.File;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.barrikeit.application.ApplicationProperties;
import org.barrikeit.config.MvcConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@Log4j2
public class ContainerFactory {

  private ContainerFactory() {}

  public static void start(AnnotationConfigApplicationContext mainContext) {
    Tomcat tomcat = embeddedTomcat(mainContext);
    startTomcat(tomcat);
  }

  private static Tomcat embeddedTomcat(AnnotationConfigApplicationContext mainContext) {
    // Get the serverProperties from the main context loaded from the application files
    ApplicationProperties.GenericProperties applicationProperties =
        mainContext.getBean(ApplicationProperties.GenericProperties.class);
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
    AnnotationConfigWebApplicationContext applicationContext =
        new AnnotationConfigWebApplicationContext();
    applicationContext.setParent(mainContext);
    applicationContext.register(MvcConfiguration.class);
    applicationContext.setServletContext(rootContext.getServletContext());
    applicationContext.refresh();

    // Set up the DispatcherServlet
    DispatcherServlet dispatcherServlet = new DispatcherServlet(applicationContext);
    Tomcat.addServlet(rootContext, applicationProperties.getName(), dispatcherServlet);
    rootContext.addServletMappingDecoded(serverProperties.getApiPath(), applicationProperties.getName());
    return tomcat;
  }

  private static void startTomcat(Tomcat tomcat) {
    try {
      tomcat.start();

      String url = tomcat.getHost().getName() + ":" + tomcat.getConnector().getLocalPort();
      log.debug("Application started on {}", url);
      // UserController controller = applicationContext.getBean(UserController.class);
      // controller.save(UserDto.builder().username("username").email("mail@generic.es").build());

      tomcat.getServer().await();
    } catch (LifecycleException e) {
      log.error("Failed to start Application", e);
    }
  }
}
