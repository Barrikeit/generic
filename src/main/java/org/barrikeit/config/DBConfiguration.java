package org.barrikeit.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Se pueden obtener los valores de la configuración del yaml,yml,xml
 * <li>Asignando @ConfigurationProperties(prefix = "spring.datasource") a la clase y
 *     añadiendo @EnableConfigurationProperties(Configuration.class) al main
 * <li>Poniéndole @Value("${spring.datasource.#elcampo}) en cada campo
 */
@Getter
@Configuration
public class DBConfiguration {
  @Value("${spring.datasource.path}${spring.datasource.database}")
  private String path;

  @Value("${spring.datasource.driver}")
  private String driver;

  @Value("${spring.datasource.username}")
  private String username;

  @Value("${spring.datasource.password}")
  private String password;
}
