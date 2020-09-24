package fr.esupportail.esupstage.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties("application")
public class ApplicationProperties {

	/**
	 * Use as application name in mails and MANIFEST
	 */
	private String title;

	/**
	 * Where to redirect the application on login.
	 */
	private String redirectUrl;

	@NestedConfigurationProperty
	private CORSProperties cors = new CORSProperties();

	/**
	 * Configuration for CSRF protection.
	 */
	@NestedConfigurationProperty
	private CSRFProperties csrf = new CSRFProperties();

}