package org.barrikeit.application;

import java.util.Properties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * <b>Database Configuration Class</b>
 *
 * <p>This class is responsible for reading the database configuration values defined in the
 * application's configuration file (like .yml, .yaml, or .properties). It supports two main
 * approaches for obtaining these values:
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
@Configuration
public class ApplicationProperties {
  public ApplicationProperties() {}

  @Getter
  @Setter
  @Component
  public static class GenericProperties {
    @Value("${generic.name}")
    private String name;
  }

  @Getter
  @Setter
  @Component
  public static class ServerProperties {
    @Value("${server.port}")
    private int port;

    @Value("${server.name}")
    private String name;

    @Value("${server.contextPath}")
    private String contextPath;

    @Value("${server.apiPath}")
    private String apiPath;

    @Value("${server.module}")
    private String module;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${server.servlet.encoding.force-response}")
    private boolean forceResponse;
  }

  @Getter
  @Setter
  @Component
  public static class DatabaseProperties {
    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.jpa.database}")
    private String database;

    @Value("${spring.jpa.database-platform}")
    private String dialect;

    @Value("${spring.jpa.generate-ddl}")
    private String generateDdl;

    @Value("${spring.jpa.open-in-view}")
    private String openInView;

    @Value("${spring.jpa.properties.hibernate.synonyms}")
    private String synonyms;

    @Value("${spring.jpa.properties.hibernate.format_sql}")
    private String formatSql;

    @Value("${spring.jpa.properties.hibernate.show_sql}")
    private String showSql;

    @Value("${spring.jpa.properties.hibernate.default_schema}")
    private String defaultSchema;

    @Value("${spring.jpa.properties.hibernate.hbm2ddl.auto}")
    private String hbm2ddlAuto;

    @Value("${spring.jpa.properties.hibernate.hbm2ddl.import_files}")
    private String importFiles;

    @Value("${spring.jpa.properties.hibernate.generate_statistics}")
    private String generateStatistics;

    @Value("${spring.jpa.properties.hibernate.enable_lazy_load_no_trans}")
    private String enableLazyLoadNoTrans;

    public Properties properties() {
      Properties properties = new Properties();
      properties.put("hibernate.dialect", getDialect());
      properties.put("hibernate.show_sql", getShowSql());
      properties.put("hibernate.format_sql", getFormatSql());
      properties.put("hibernate.hbm2ddl.auto", getHbm2ddlAuto());
      properties.put("hibernate.hbm2ddl.import_files", getImportFiles());
      properties.put("hibernate.generate_statistics", getGenerateStatistics());
      properties.put("hibernate.jdbc.batch_size", "5");
      properties.put("hibernate.default_batch_fetch_size", "10");
      return properties;
    }
  }
}
