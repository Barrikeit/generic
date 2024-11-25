package org.barrikeit.config;

import javax.sql.DataSource;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.barrikeit.application.ApplicationProperties;
import org.barrikeit.util.constants.ConfigurationConstants;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * <b>Hibernate Configuration Class</b>
 *
 * <p>This configuration class sets up the necessary beans for integrating Hibernate as a standalone
 * ORM framework. It is responsible for configuring Hibernate-specific settings through the {@link
 * LocalSessionFactoryBean} and {@link HibernateTransactionManager}, which facilitate the management
 * of JPA entities using the Hibernate API.
 *
 * <p>Properties specific to Hibernate, such as SQL visibility, batching, and dialect, are sourced
 * from {@link ApplicationProperties} and configured accordingly.
 *
 * <p>This configuration focuses on leveraging Hibernate’s native API, making it useful when
 * advanced Hibernate-specific features (such as caching strategies or batch processing) are
 * required that may not be fully supported by standard JPA.
 */
@Log4j2
@Configuration
@AllArgsConstructor
@EnableTransactionManagement
@EnableJpaRepositories(
    value = {ConfigurationConstants.REPOSITORIES_PACKAGE},
    entityManagerFactoryRef = "hibernateSessionFactory",
    transactionManagerRef = "hibernateTransactionManager")
public class HibernateConfiguration {

  private final ApplicationProperties.DatabaseProperties dbProperties;

  /**
   * Creates a {@link LocalSessionFactoryBean} for managing Hibernate session lifecycle.
   *
   * <p>This bean is responsible for creating a {@link SessionFactory}, which is used to manage
   * Hibernate sessions. It sets the data source, configures the packages to scan for entities, and
   * applies the properties defined in {@link
   * ApplicationProperties.DatabaseProperties#properties()}.
   *
   * @param dataSource the {@link DataSource} used to connect to the database.
   * @return a configured {@link LocalSessionFactoryBean} instance.
   */
  @Bean(name = "hibernateSessionFactory")
  public LocalSessionFactoryBean hibernateSessionFactory(DataSource dataSource) {
    log.info("***Creating Hibernate SessionFactory");
    LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
    sessionFactory.setDataSource(dataSource);
    sessionFactory.setPackagesToScan(ConfigurationConstants.ENTITIES_PACKAGE);
    sessionFactory.setHibernateProperties(dbProperties.properties());
    return sessionFactory;
  }

  /**
   * Configures the {@link HibernateTransactionManager} for managing Hibernate transactions.
   *
   * <p>This bean integrates transaction management into the application, allowing for atomic
   * operations over multiple entities. The transaction manager uses the {@link SessionFactory}
   * created by {@link #hibernateSessionFactory(DataSource dataSource)} for transaction management.
   *
   * @param sessionFactory the Hibernate {@link SessionFactory} for transaction management.
   * @return a configured {@link HibernateTransactionManager} instance.
   */
  @Bean(name = "hibernateTransactionManager")
  public HibernateTransactionManager hibernateTransactionManager(
      @Qualifier("hibernateSessionFactory") SessionFactory sessionFactory) {
    log.info("***Creating Hibernate TransactionManager");
    HibernateTransactionManager transactionManager = new HibernateTransactionManager();
    transactionManager.setSessionFactory(sessionFactory);
    return transactionManager;
  }
}
