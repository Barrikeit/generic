package org.barrikeit.config;

import java.io.File;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Log4j2
@Configuration
@AllArgsConstructor
public class ServletContainerFactory {

  private final ApplicationProperties.ServerProperties serverProperties;
  private final ServletInitializer servletInitializer;
  private final String warPath = "dist/generic.war";
  private final String classesPath = "target/classes";

  @Bean
  public Tomcat tomcatFactory() {
    Tomcat tomcat = new Tomcat();
    tomcat.setPort(serverProperties.getPort());
    tomcat.setSilent(true);
    tomcat.setAddDefaultWebXmlToWebapp(false);

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
      context.addServletContainerInitializer((c, ctx) -> servletInitializer.onStartup(ctx), null);
    }
    return tomcat;
  }
}
