package org.barrikeit.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Log4j2
@Configuration
@ComponentScan(basePackages = ApplicationConfiguration.COMPONENT_PACKAGE_TO_SCAN)
public class ApplicationConfiguration {
  public ApplicationConfiguration() {}

  public static final String COMPONENT_PACKAGE_TO_SCAN = "org.barrikeit";
  public static final String REST_PACKAGE_TO_SCAN = "org.barrikeit.rest";
  public static final String REPOSITORY_PACKAGE_TO_SCAN = "org.barrikeit.repository";
  public static final String JPA_PACKAGE_TO_SCAN = "org.barrikeit.domain";

  private static final String[] CONFIG_LOCATIONS = {"/", "/config/", "/configuration/"};
  private static final String[] CONFIG_EXTENSIONS = {"properties", "yml", "yaml"};

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

  private static boolean loadConfigFiles(MutablePropertySources propertySources) {
    for (String path : CONFIG_LOCATIONS) {
      for (String extension : CONFIG_EXTENSIONS) {
        if (loadFileIntoPropertySources(
            propertySources, resolvePath(path, "application." + extension))) {
          List<String> configImports = getPropertyList(propertySources, "spring.config.import");
          configImports.forEach(
              importFile ->
                  loadFileIntoPropertySources(propertySources, resolvePath(path, importFile)));
          return true; // Return on first successful load
        }
      }
    }
    return false;
  }

  private static boolean loadFileIntoPropertySources(
      MutablePropertySources propertySources, String filePath) {
    Resource resource = new ClassPathResource(filePath);
    if (!resource.exists()) {
      // log.warn(
      // "class path resource [{}] cannot be resolved to URL because it does not exist",
      // filePath);
      return false;
    }

    log.info("Reading config file: {}", filePath);
    if (filePath.endsWith(".properties")) {
      return loadPropertiesFile(propertySources, resource, filePath);
    } else if (filePath.endsWith(".yaml") || filePath.endsWith(".yml")) {
      return loadYamlFile(propertySources, resource, filePath);
    }
    return false;
  }

  private static boolean loadPropertiesFile(
      MutablePropertySources propertySources, Resource resource, String propertiesFile) {
    try (InputStream input = resource.getInputStream()) {
      Properties properties = new Properties();
      properties.load(input);
      propertySources.addLast(new PropertiesPropertySource(propertiesFile, properties));
      return true;
    } catch (IOException e) {
      log.warn("Cannot resolve path for properties file: {}", propertiesFile, e);
      return false;
    }
  }

  private static boolean loadYamlFile(
      MutablePropertySources propertySources, Resource resource, String yamlFile) {
    YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
    yaml.setResources(resource);
    Properties properties = yaml.getObject();
    if (properties != null) {
      propertySources.addLast(new PropertiesPropertySource(yamlFile, properties));
      return true;
    }
    log.warn("Failed to load YAML file: {}", yamlFile);
    return false;
  }

  private static String resolvePath(String basePath, String importFile) {
    if (importFile.startsWith("classpath:") || importFile.startsWith("/")) {
      return importFile;
    }
    basePath = basePath.endsWith("/") ? basePath.substring(0, basePath.length() - 1) : basePath;
    return (importFile.startsWith("/") || importFile.startsWith("../"))
        ? basePath + importFile
        : basePath + "/" + importFile;
  }

  private static List<String> getPropertyList(
      MutablePropertySources propertySources, String keyPrefix) {
    List<String> values = new ArrayList<>();
    propertySources.forEach(
        source -> {
          if (source instanceof EnumerablePropertySource<?> enumerableSource) {
            Arrays.stream(enumerableSource.getPropertyNames())
                .filter(propertyName -> propertyName.startsWith(keyPrefix))
                .map(enumerableSource::getProperty)
                .filter(value -> value instanceof String)
                .map(String.class::cast)
                .forEach(values::add);
          }
        });
    return values;
  }
}
