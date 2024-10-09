package org.barrikeit.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = "org.barrikeit")
@EnableJpaRepositories(basePackages = "org.barrikeit.repository")
public class ApplicationConfiguration {}
