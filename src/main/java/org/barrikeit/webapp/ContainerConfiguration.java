package org.barrikeit.webapp;

import lombok.extern.log4j.Log4j2;
import org.barrikeit.util.constants.ConfigurationConstants;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Log4j2
@Configuration
@ComponentScan(basePackages = ConfigurationConstants.WEBAPP_PACKAGE_TO_SCAN)
public class ContainerConfiguration {
  public ContainerConfiguration() {}
}
