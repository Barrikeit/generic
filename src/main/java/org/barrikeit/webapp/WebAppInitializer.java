package org.barrikeit.webapp;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import java.util.EnumSet;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.barrikeit.config.ApplicationConfiguration;
import org.barrikeit.config.ApplicationProperties;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.core.Conventions;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@Log4j2
@Component
@AllArgsConstructor
public class WebAppInitializer implements WebApplicationInitializer {

  private final ApplicationProperties.ServerProperties serverProperties;

  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {
    WebApplicationContext rootAppContext = createRootApplicationContext();
    if (rootAppContext != null) {
      ContextLoaderListener listener = new ContextLoaderListener(rootAppContext);
      listener.setContextInitializers(getRootApplicationContextInitializers());
      servletContext.addListener(listener);
    } else {
      log.warn(
          "No ContextLoaderListener registered, as createRootApplicationContext() did not return an application context");
    }
    this.registerDispatcherServlet(servletContext);
  }

  private WebApplicationContext createRootApplicationContext() {
    Class<?>[] configClasses = getRootConfigClasses();
    if (!ObjectUtils.isEmpty(configClasses)) {
      AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
      context.register(configClasses);
      return context;
    } else {
      return null;
    }
  }

  private ApplicationContextInitializer<?>[] getRootApplicationContextInitializers() {
    return null;
  }

  private void registerDispatcherServlet(ServletContext servletContext) {
    String servletName = getServletName();
    WebApplicationContext context = createApplicationContext();
    DispatcherServlet dispatcherServlet = new DispatcherServlet(context);
    dispatcherServlet.setContextInitializers(); // pasarle un inicializador del contexto
    ServletRegistration.Dynamic registration =
        servletContext.addServlet(servletName, dispatcherServlet);
    if (registration != null) {
      registration.setLoadOnStartup(1);
      registration.addMapping(getServletMappings());
      registration.setAsyncSupported(isAsyncSupported());
      Filter[] filters = getServletFilters();
      if (!ObjectUtils.isEmpty(filters)) {
        Filter[] var7 = filters;
        int var8 = filters.length;

        for (int var9 = 0; var9 < var8; ++var9) {
          Filter filter = var7[var9];
          registerServletFilter(servletContext, filter);
        }
      }
      customizeRegistration(registration);
      log.info("***Web application initialized");
    } else {
      log.error(
          "Failed to register servlet with name '{}'. Check if there is another servlet registered under the same name.",
          servletName);
    }
  }

  private String getServletName() {
    return serverProperties.getName();
  }

  private AnnotationConfigWebApplicationContext createApplicationContext() {
    AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
    Class<?>[] configClasses = getConfigClasses();
    if (!ObjectUtils.isEmpty(configClasses)) {
      context.register(configClasses);
    } else {
      log.error("No Spring configuration provided through getConfigClasses()");
    }
    return context;
  }

  private Class<?>[] getRootConfigClasses() {
    return new Class<?>[] {};
  }

  private Class<?>[] getConfigClasses() {
    return new Class<?>[] {ApplicationConfiguration.class};
  }

  private String[] getServletMappings() {
    return new String[] {"/", serverProperties.getApiPath()};
  }

  private boolean isAsyncSupported() {
    return true;
  }

  private Filter[] getServletFilters() {
    return null;
  }

  private FilterRegistration.Dynamic registerServletFilter(
      ServletContext servletContext, Filter filter) {
    String filterName = Conventions.getVariableName(filter);
    FilterRegistration.Dynamic registration = servletContext.addFilter(filterName, filter);
    if (registration == null) {
      for (int counter = 0; registration == null; ++counter) {
        if (counter == 100) {
          log.error(
              "Failed to register filter with name '{}'. Check if there is another filter registered under the same name.",
              filterName);
        }
        registration = servletContext.addFilter(filterName + "#" + counter, filter);
      }
    }

    registration.setAsyncSupported(isAsyncSupported());
    registration.addMappingForServletNames(
        getDispatcherTypes(), false, new String[] {getServletName()});
    return registration;
  }

  private EnumSet<DispatcherType> getDispatcherTypes() {
    return this.isAsyncSupported()
        ? EnumSet.of(
            DispatcherType.REQUEST,
            DispatcherType.FORWARD,
            DispatcherType.INCLUDE,
            DispatcherType.ASYNC)
        : EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE);
  }

  protected void customizeRegistration(ServletRegistration.Dynamic registration) {}
}
