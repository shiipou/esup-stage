package fr.esupportail.esupstage.property;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Configuration properties to manage the CORS configuration.
 *
 * @author vdubus
 */
@Getter
@Setter
public class CORSProperties implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * The list of valid domains to allow.
	 */
	private List<String> allowedOrigins = Arrays.asList("*");

	/**
	 * The list of HTTP methods to allow.
	 */
	private List<String> allowedMethods = Arrays.asList("*");

	/**
	 * The list of HTTP headers to allow.
	 */
	private List<String> allowedHeaders = Arrays.asList("*");

	/**
	 * The list of HTTP exposed header.
	 */
	private List<String> exposedHeaders = Arrays.asList("Content-Disposition");

}
