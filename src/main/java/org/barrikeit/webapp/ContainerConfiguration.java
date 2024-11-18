package org.barrikeit.webapp;

import lombok.extern.log4j.Log4j2;
import org.barrikeit.config.ApplicationConfiguration;
import org.barrikeit.config.ApplicationProperties;
import org.barrikeit.util.constants.ConfigurationConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Log4j2
@Configuration
@ComponentScan(basePackages = ConfigurationConstants.WEBAPP_PACKAGE_TO_SCAN)
public class ContainerConfiguration {
  public ContainerConfiguration() {}

  @Bean
  public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    return ApplicationConfiguration.propertySourcesPlaceholderConfigurer();
  }
}
