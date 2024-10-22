package org.barrikeit.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@Log4j2
@Configuration
@AllArgsConstructor
public class WebApplicationInitializer
    implements org.springframework.web.WebApplicationInitializer {

  @Override
  public void onStartup(ServletContext container) {
    log.info("***Web application instantiated.");

    // Creates context object and sets ContextLoaderListener to servletContext
    AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
    ctx.register(ApplicationConfiguration.class);
    container.addListener(new ContextLoaderListener(ctx));

    // Register and map the dispatcher servlet
    DispatcherServlet dispatcherServlet = new DispatcherServlet(ctx);
    ServletRegistration.Dynamic servlet = container.addServlet("generic", dispatcherServlet);
    servlet.addMapping("/api/*");
    servlet.setLoadOnStartup(1);
  }
}
