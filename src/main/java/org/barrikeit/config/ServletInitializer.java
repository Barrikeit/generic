package org.barrikeit.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@Log4j2
@Configuration
@AllArgsConstructor
public class ServletInitializer implements WebApplicationInitializer {

  @Override
  public void onStartup(ServletContext servletContext) {
    log.info("***Web application initialized.");
    // Creates context object and sets ContextLoaderListener to servletContext
    AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
    ctx.register(ApplicationConfiguration.class);
    servletContext.addListener(new ContextLoaderListener(ctx));
    // Register and map the dispatcher servlet
    DispatcherServlet dispatcherServlet = new DispatcherServlet(ctx);
    ServletRegistration.Dynamic servlet = servletContext.addServlet("generic", dispatcherServlet);
    servlet.addMapping("/api/*");
    servlet.setLoadOnStartup(1);
  }
}
