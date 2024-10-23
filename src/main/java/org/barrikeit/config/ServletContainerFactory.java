package org.barrikeit.config;

import java.io.File;
import java.nio.file.Path;

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

  private final String distDir = Path.of(System.getProperty("user.dir") + "/dist").toAbsolutePath().toString();
  private final String webappDir = Path.of(distDir, "webapps/ROOT").toAbsolutePath().toString();
  private final String warFilePath =Path.of(distDir + "/generic.war").toAbsolutePath().toString();
  private final ApplicationProperties.ServerProperties serverProperties;

  @Bean
  public Tomcat tomcatFactory() {
    ensureDirectoryExists(distDir);
    ensureDirectoryExists(webappDir);

    Tomcat tomcat = new Tomcat();
    tomcat.setPort(serverProperties.getPort());
    tomcat.setSilent(true);
    tomcat.setBaseDir(distDir);
    tomcat.setAddDefaultWebXmlToWebapp(false);

    log.info("Deploying application from war: {}", warFilePath);
    Context context = tomcat.addWebapp("", warFilePath);

    customizeContext(context);
    return tomcat;
  }

  private void ensureDirectoryExists(String path) {
    File directory = new File(path);
    if (!directory.exists()) {
      log.info("Creating directory: {}", path);
      if (directory.mkdirs()) {
        log.info("Directory created successfully: {}", path);
      } else {
        log.error("Failed to create directory: {}", path);
      }
    }
  }

  private void customizeContext(Context context) {}
}
