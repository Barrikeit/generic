package org.barrikeit.application;

import static org.barrikeit.util.FileUtil.loadConfigFiles;

import lombok.extern.log4j.Log4j2;
import org.barrikeit.util.constants.ConfigurationConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.MutablePropertySources;

@Log4j2
@Configuration
@ComponentScan(basePackages = ConfigurationConstants.APPLICATION_PACKAGE)
public class ApplicationConfiguration {
  public ApplicationConfiguration() {}

  @Bean
  public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
    MutablePropertySources propertySources = new MutablePropertySources();

    if (loadConfigFiles(propertySources)) {
      configurer.setPropertySources(propertySources);
    }

    configurer.setIgnoreUnresolvablePlaceholders(true);
    configurer.setIgnoreResourceNotFound(true);
    return configurer;
  }
}
