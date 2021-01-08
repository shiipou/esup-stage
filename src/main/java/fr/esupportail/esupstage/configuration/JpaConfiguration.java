package fr.esupportail.esupstage.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import fr.esupportail.esupstage.configuration.jpa.AuthenticationAuditorAware;

/**
 * @author vdubus
 */
@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "fr.esupportail.esupstage.domain.jpa")
public class JpaConfiguration {

	@Bean
	public AuditorAware<String> auditorProvider() {
		return new AuthenticationAuditorAware();
	}

}
