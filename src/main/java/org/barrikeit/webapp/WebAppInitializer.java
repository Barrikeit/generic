package org.barrikeit.webapp;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.barrikeit.config.ApplicationConfiguration;
import org.barrikeit.config.ApplicationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@Log4j2
@Component
@AllArgsConstructor
public class WebAppInitializer implements WebApplicationInitializer {

  private final ServerProperties serverProperties;

  @Override
  public void onStartup(ServletContext servletContext) {
    AnnotationConfigWebApplicationContext applicationContext = createApplicationContext();
    applicationContext.refresh();
    servletContext.addListener(new WebAppContextListener(applicationContext));

    DispatcherServlet servlet = new DispatcherServlet(applicationContext);
    ServletRegistration.Dynamic dispatcher =
        servletContext.addServlet(serverProperties.getName(), servlet);

    dispatcher.addMapping(getServletMapping());
    dispatcher.setAsyncSupported(true);
    dispatcher.setLoadOnStartup(1);
    log.info("***Web application initialized");
  }

  private AnnotationConfigWebApplicationContext createApplicationContext() {
    AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
    Class<?>[] configClasses = getConfigClasses();
    Assert.state(
        !ObjectUtils.isEmpty(configClasses),
        "No Spring configuration provided through getConfigClasses()");
    context.register(configClasses);
    return context;
  }

  private Class<?>[] getConfigClasses() {
    return new Class<?>[] {ApplicationConfiguration.class};
  }

  private String[] getServletMapping() {
    return new String[] {"/", serverProperties.getApiPath()};
  }
}
