package org.barrikeit.config;

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.barrikeit.application.ApplicationProperties;
import org.barrikeit.util.constants.ConfigurationConstants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * <b>JPA Configuration Class</b>
 *
 * <p>This configuration class sets up the necessary beans for JPA integration using Hibernate as
 * the underlying persistence provider. It enables transaction management and sets up JPA
 * repositories for entity management. The class is responsible for configuring the {@link
 * LocalContainerEntityManagerFactoryBean} and {@link JpaTransactionManager}, allowing for the
 * management of JPA entities through a standard interface.
 *
 * <p>The JPA-specific properties such as dialect, SQL formatting, and batch sizes are sourced from
 * {@link ApplicationProperties} and configured for Hibernate.
 *
 * <p>This configuration is vendor-independent, meaning it adheres to the JPA specification, which
 * allows for flexibility in switching to other JPA providers if needed.
 */
@Log4j2
@Configuration
@AllArgsConstructor
@EnableTransactionManagement
@EnableJpaRepositories(
    value = {ConfigurationConstants.REPOSITORIES_PACKAGE},
    entityManagerFactoryRef = "jpaEntityManagerFactory",
    transactionManagerRef = "jpaTransactionManager")
public class JpaConfiguration {

  private final ApplicationProperties.DatabaseProperties dbProperties;

  /**
   * Creates a {@link LocalContainerEntityManagerFactoryBean} for managing JPA entity lifecycle.
   *
   * <p>This bean is responsible for creating an {@link EntityManagerFactory}, which is used to
   * manage JPA entities. It sets the data source, configures the JPA vendor adapter, and applies
   * the properties defined in {@link ApplicationProperties.DatabaseProperties#properties()}.
   *
   * @param dataSource the {@link DataSource} used to connect to the database.
   * @return a configured {@link LocalContainerEntityManagerFactoryBean} instance.
   */
  @Primary
  @Bean(name = "jpaEntityManagerFactory")
  public LocalContainerEntityManagerFactoryBean jpaEntityManagerFactory(DataSource dataSource) {
    log.info("***Creating JPA entity manager factory");
    LocalContainerEntityManagerFactoryBean entityManagerFactoryBean =
        new LocalContainerEntityManagerFactoryBean();
    entityManagerFactoryBean.setDataSource(dataSource);
    entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    entityManagerFactoryBean.setPackagesToScan(ConfigurationConstants.ENTITIES_PACKAGE);
    entityManagerFactoryBean.setJpaProperties(dbProperties.properties());
    return entityManagerFactoryBean;
  }

  /**
   * Configures the {@link JpaTransactionManager} for managing JPA transactions.
   *
   * <p>This bean integrates transaction management into the application, allowing for atomic
   * operations over multiple entities. The transaction manager uses the {@link
   * EntityManagerFactory} created by {@link #jpaEntityManagerFactory(DataSource dataSource)} for
   * transaction management.
   *
   * @param entityManagerFactory the JPA {@link EntityManagerFactory} for transaction management.
   * @return a configured {@link JpaTransactionManager} instance.
   */
  @Primary
  @Bean(name = "jpaTransactionManager")
  public PlatformTransactionManager jpaTransactionManager(
      @Qualifier("jpaEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
    log.info("***Creating JPA transaction manager");
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(entityManagerFactory);
    return transactionManager;
  }
}
