package org.barrikeit.webapp;

import static org.barrikeit.util.FileUtil.createTempFolder;

import java.io.File;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class ContainerFactory {

  private final ServerProperties serverProperties;

  public ContainerFactory(AnnotationConfigApplicationContext springContext) {
    this.serverProperties = springContext.getBean(ServerProperties.class);
  }

  public void start() {
    try {
      Tomcat tomcat = embeddedTomcat();
      tomcat.start();
      String url = tomcat.getHost().getName() + ":" + tomcat.getConnector().getLocalPort();
      log.info("Tomcat started on {}", url);

      // UserController userController = context.getBean(UserController.class);
      // userController.save(UserDto.builder().username("username").email("email@e.es").build());

      tomcat.getServer().await();
    } catch (LifecycleException e) {
      log.error("Failed to start Tomcat", e);
    }
  }

  private Tomcat embeddedTomcat() {
    int port = serverProperties.getPort();
    File baseDir = createTempFolder("embedded-tomcat", port);
    File webappDir = createTempFolder("embedded-webapp", port);
    Tomcat tomcat = new Tomcat();
    tomcat.setPort(port);
    tomcat.setSilent(true);
    tomcat.setAddDefaultWebXmlToWebapp(false);
    tomcat.setBaseDir(baseDir.getAbsolutePath());
    tomcat.getConnector();

    log.info("***Deploying application from: {}", webappDir.getAbsolutePath());
    Context context =
        tomcat.addWebapp(serverProperties.getContextPath(), webappDir.getAbsolutePath());
    context.addApplicationListener(WebAppInitializer.class.getName());
    return tomcat;
  }
}
