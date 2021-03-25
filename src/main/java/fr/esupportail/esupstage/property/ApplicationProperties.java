package fr.esupportail.esupstage.property;

import java.io.Serializable;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties("application")
public class ApplicationProperties implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Use as application name in mails and MANIFEST
	 */
	private String title;

	/**
	 * Where to redirect the application on login.
	 */
	private String redirectUrl;
	
	/**
	 * Configuration for the CAS authentication.
	 */
	@NestedConfigurationProperty
	private CASProperties cas = new CASProperties();

	/**
	 * Configuration for CORS.
	 */
	@NestedConfigurationProperty
	private CORSProperties cors = new CORSProperties();

	/**
	 * Configuration for CSRF protection.
	 */
	@NestedConfigurationProperty
	private CSRFProperties csrf = new CSRFProperties();

	/**
	 * Security configuration.
	 */
	@NestedConfigurationProperty
	private SecurityProperties security = new SecurityProperties();

}
