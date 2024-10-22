package org.barrikeit;

import java.io.File;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

@Log4j2
public class Main {

  public static void main(String[] args) throws LifecycleException {
    log.info("Loading application context...");

    // Initialize the web application context but don't refresh yet
    AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
    int port = 8080;
    Tomcat tomcat = new Tomcat();
    tomcat.setPort(port);
    tomcat.setSilent(true);
    tomcat.setAddDefaultWebXmlToWebapp(false);
    log.warn(
        "falta ponerle al tomcat un directorio base y asignarle al tomcat la webapp mediante el archivo .war");
    tomcat.setBaseDir("");
    Context context = tomcat.addWebapp("", File.separatorChar + "generic.war");
    rootContext.setServletContext(context.getServletContext());
    rootContext.refresh();

    tomcat.start();
    log.info("Tomcat started on port {}", port);
    tomcat.getServer().await();
  }
}
