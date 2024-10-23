package org.barrikeit.config;

import java.util.Properties;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;

@Log4j2
@Configuration
@EnableScheduling
@AllArgsConstructor
@ComponentScan(basePackages = ApplicationConfiguration.COMPONENT_PACKAGE_TO_SCAN)
public class ApplicationConfiguration {

  public static final String COMPONENT_PACKAGE_TO_SCAN = "org.barrikeit";
  public static final String REST_PACKAGE_TO_SCAN = "org.barrikeit.rest";
  public static final String REPOSITORY_PACKAGE_TO_SCAN = "org.barrikeit.repository";
  public static final String JPA_PACKAGE_TO_SCAN = "org.barrikeit.domain";
  public static final String PROPERTIES_FILE = "config/application.yaml";
  public static final String BASE_PROPERTIES_FILE = "config/application-base.yaml";

  @Bean
  public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(ConfigurableEnvironment environment) {
    log.info("***Reading properties files at " + PROPERTIES_FILE);
    PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
    MutablePropertySources propertySources = new MutablePropertySources();

    loadYamlIntoPropertySources(propertySources, PROPERTIES_FILE);
    loadYamlIntoPropertySources(propertySources, BASE_PROPERTIES_FILE);

    String activeProfile = environment.getActiveProfiles().length > 0 ? environment.getActiveProfiles()[0] : "default";
    String profileSpecificFile = "config/application-" + activeProfile + ".yml";
    loadYamlIntoPropertySources(propertySources, profileSpecificFile);

    configurer.setPropertySources(propertySources);
    configurer.setIgnoreResourceNotFound(true);
    configurer.setIgnoreUnresolvablePlaceholders(true);
    return configurer;
  }

  private static void loadYamlIntoPropertySources(MutablePropertySources propertySources, String yamlFile) {
    ClassPathResource resource = new ClassPathResource(yamlFile);
    if (resource.exists()) {
      YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
      yaml.setResources(resource);
      Properties properties = yaml.getObject();

      if (properties != null) {
        propertySources.addLast(new PropertiesPropertySource(yamlFile, properties));
      }
    } else {
      log.warn("YAML file not found: {}", yamlFile);
    }
  }
}
