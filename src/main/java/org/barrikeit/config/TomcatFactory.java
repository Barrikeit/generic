package org.barrikeit.config;

import java.io.File;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.barrikeit.config.webapp.ServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

@Log4j2
@Configuration
@AllArgsConstructor
public class TomcatFactory {

  private final AnnotationConfigWebApplicationContext springContext;
  private final String warPath = "dist/generic.war";
  private final String classesPath = "target/classes";

  @Bean
  public Tomcat embeddedTomcat() {
    ApplicationProperties.ServerProperties serverProperties = springContext.getBean(ApplicationProperties.ServerProperties.class);
    Tomcat tomcat = new Tomcat();
    tomcat.setPort(serverProperties.getPort());
    tomcat.setSilent(true);
    tomcat.setAddDefaultWebXmlToWebapp(false);
    tomcat.getConnector();

    if (serverProperties.isStandaloneWar()) {
      File warFile = new File(warPath);
      if (!warFile.exists()) {
        throw new IllegalStateException("WAR file not found at " + warFile.getAbsolutePath());
      }
      log.info("Deploying application from WAR: {}", warFile.getAbsolutePath());
      tomcat.addWebapp("", warFile.getAbsolutePath());
    } else {
      File base = new File(classesPath);
      Context context = tomcat.addContext("/generic", base.getAbsolutePath());

      log.info("Deploying application from target/classes: {}", base.getAbsolutePath());
      context.addServletContainerInitializer(
          (c, ctx) -> {
            ServletInitializer servletInitializer = new ServletInitializer(springContext);
            servletInitializer.onStartup(ctx);
          },
          null);
    }
    return tomcat;
  }
}
