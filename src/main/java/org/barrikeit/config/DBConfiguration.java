package org.barrikeit.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * <b>Database Configuration Class</b>
 *
 * <p>This class is responsible for reading the database configuration values defined in the
 * application's configuration file (like application.yml, application.yaml, or
 * application.properties). It supports two main approaches for obtaining these values:
 *
 * <ul>
 *   <li>Using {@code @ConfigurationProperties(prefix = "spring.datasource")}: This binds all
 *       properties under the specified prefix directly to the fields of the class. You need to
 *       enable this in the main class by adding
 *       {@code @EnableConfigurationProperties(DBConfiguration.class)}.
 *   <li>Using {@code @Value("${spring.datasource.<field>}")}: This directly injects individual
 *       values from the configuration file into the respective fields of this class.
 * </ul>
 *
 * <p>This class uses the {@code @Value} annotation approach to inject database-related properties
 * such as URL, driver, username, and password.
 */
@Getter
@Configuration
public class DBConfiguration {

  @Value("${spring.datasource.url}")
  private String path;

  @Value("${spring.datasource.driverClassName}")
  private String driver;

  @Value("${spring.datasource.username}")
  private String username;

  @Value("${spring.datasource.password}")
  private String password;
}
