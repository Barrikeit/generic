package org.barrikeit.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

//TODO: if @config is set here error appears (resourcehandlermapping no servletcontext set)
@Log4j2
//@Configuration
@EnableWebMvc
@ComponentScan(basePackages = ApplicationConfiguration.REST_PACKAGE_TO_SCAN)
public class WebMvcConfiguration implements WebMvcConfigurer {

  public WebMvcConfiguration() {
    log.info("***ApplicationMVC instantiated");
  }
}
