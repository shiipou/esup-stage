package fr.esupportail.esupstage.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author vdubus
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "fr.esupportail.esupstage.domain.jpa")
public class JpaConfiguration {

}
