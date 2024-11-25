package org.barrikeit.config;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.barrikeit.application.ApplicationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Log4j2
@Configuration
@AllArgsConstructor
public class JdbcConfiguration {

  private final ApplicationProperties.DatabaseProperties dbProperties;

  @Bean
  public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    return new PropertySourcesPlaceholderConfigurer();
  }

  @Bean
  public DriverManagerDataSource dataSource() {
    log.info("***Creating a datasource for {}", dbProperties.getDriverClassName());
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(dbProperties.getDriverClassName());
    dataSource.setUrl(dbProperties.getUrl());
    dataSource.setUsername(dbProperties.getUsername());
    dataSource.setPassword(dbProperties.getPassword());
    dataSource.setSchema(dbProperties.getDefaultSchema());
    return dataSource;
  }

  @Bean
  public PasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }
}
