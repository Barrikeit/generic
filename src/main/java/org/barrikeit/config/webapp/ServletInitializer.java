package org.barrikeit.config.webapp;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@Log4j2
@Configuration
@AllArgsConstructor
public class ServletInitializer {

  private final AnnotationConfigWebApplicationContext springContext;

  public void onStartup(ServletContext servletContext) {
    servletContext.addListener(new SpringContextLoaderListener(springContext));

    DispatcherServlet dispatcherServlet = new DispatcherServlet(springContext);
    ServletRegistration.Dynamic dispatcher =
        servletContext.addServlet("dispatcher", dispatcherServlet);
    dispatcher.addMapping("/", "/api/*");
    dispatcher.setLoadOnStartup(1);
    log.info("***Web application initialized.");
  }
}
