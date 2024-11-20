package org.barrikeit.webapp;

import static org.barrikeit.util.FileUtil.createTempFolder;

import java.io.File;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.barrikeit.config.ApplicationProperties;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@AllArgsConstructor
public class ContainerFactory {

  private final AnnotationConfigApplicationContext springContext;

  public Tomcat embeddedTomcat() {
    ApplicationProperties.ServerProperties serverProperties =
        springContext.getBean(ApplicationProperties.ServerProperties.class);
    int port = serverProperties.getPort();
    File baseDir = createTempFolder("embedded-tomcat", port);
    File webappDir = createTempFolder("embedded-webapp", port);
    Tomcat tomcat = new Tomcat();
    tomcat.setPort(port);
    tomcat.setSilent(true);
    tomcat.setAddDefaultWebXmlToWebapp(false);
    tomcat.setBaseDir(baseDir.getAbsolutePath());
    tomcat.getConnector();

    log.info("***Creating application container at: {}", webappDir.getAbsolutePath());
    Context context =
        tomcat.addWebapp(serverProperties.getContextPath(), webappDir.getAbsolutePath());
    context.addApplicationListener(WebAppInitializer.class.getName());
    return tomcat;
  }
}
