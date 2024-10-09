package org.barrikeit.config;

import java.util.Properties;
import javax.sql.DataSource;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

@Log4j2
@Configuration
@AllArgsConstructor
public class HibernateConfiguration {

  public static final String JPA_PACKAGE_TO_SCAN = "org.barrikeit";

  private final DatabaseProperties dbProperties;

  public Properties hibernateProperties() {
    Properties properties = new Properties();
    properties.put("hibernate.dialect", dbProperties.getDialect());
    properties.put("hibernate.show_sql", dbProperties.getShowSql());
    properties.put("hibernate.format_sql", dbProperties.getFormatSql());
    properties.put("hibernate.hbm2ddl.auto", dbProperties.getHbm2ddlAuto());
    properties.put("hibernate.hbm2ddl.import_files", dbProperties.getImportFiles());
    properties.put("hibernate.generate_statistics", dbProperties.getGenerateStatistics());
    properties.put("hibernate.jdbc.batch_size", "5");
    properties.put("hibernate.default_batch_fetch_size", "10");

    return properties;
  }

  @Bean
  public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
    LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
    sessionFactory.setDataSource(dataSource);
    sessionFactory.setPackagesToScan(HibernateConfiguration.JPA_PACKAGE_TO_SCAN);
    sessionFactory.setHibernateProperties(hibernateProperties());
    return sessionFactory;
  }
}
