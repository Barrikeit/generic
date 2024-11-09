package org.barrikeit.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@Log4j2
@Component
@AllArgsConstructor
public class ServletInitializer implements WebApplicationInitializer {

  private final AnnotationConfigWebApplicationContext springContext;

  @Override
  public void onStartup(ServletContext servletContext) {
    servletContext.addListener(new ContextLoaderListener(springContext));
    springContext.setServletContext(servletContext);

    DispatcherServlet dispatcherServlet = new DispatcherServlet(springContext);
    ServletRegistration.Dynamic dispatcher =
        servletContext.addServlet("generic", dispatcherServlet);
    dispatcher.addMapping("/api/*");
    dispatcher.setLoadOnStartup(1);
    log.info("***Web application initialized.");
  }
}
