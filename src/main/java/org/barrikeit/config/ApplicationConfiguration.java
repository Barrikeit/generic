package org.barrikeit.config;

import java.util.Properties;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.ClassPathResource;

@Log4j2
@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = ApplicationConfiguration.COMPONENT_PACKAGE_TO_SCAN)
public class ApplicationConfiguration {

  public static final String COMPONENT_PACKAGE_TO_SCAN = "org.barrikeit";
  public static final String REST_PACKAGE_TO_SCAN = "org.barrikeit.rest";
  public static final String REPOSITORY_PACKAGE_TO_SCAN = "org.barrikeit.repository";
  public static final String JPA_PACKAGE_TO_SCAN = "org.barrikeit.domain";
  public static final String PROPERTIES_FILE = "config/application.yaml";

  @Bean
  public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    log.info("***Reading properties file at " + PROPERTIES_FILE);
    PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
    MutablePropertySources propertySources = new MutablePropertySources();

    YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
    yaml.setResources(new ClassPathResource(PROPERTIES_FILE));
    Properties properties = yaml.getObject();
    assert properties != null;
    propertySources.addLast(new PropertiesPropertySource("yamlProperties", properties));

    configurer.setPropertySources(propertySources);
    configurer.setIgnoreResourceNotFound(true);
    configurer.setIgnoreUnresolvablePlaceholders(true);
    return configurer;
  }

  @Bean
  public PingPongAspect pingPongAspect() {
    return new PingPongAspect();
  }
}
