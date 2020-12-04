package fr.esupportail.esupstage.property;

import lombok.Getter;
import lombok.Setter;

/**
 * Configuration properties to manage the CAS authentication.
 *
 * @author vdubus
 */
@Getter
@Setter
public class CASProperties {

	/**
	 * Login end point from {@link #baseUrl}.
	 */
	private String loginEndPoint;

	/**
	 * Logout end point from {@link #baseUrl}.
	 */
	private String logoutEndPoint;

	/**
	 * The base URL of the CAS authentication server.
	 */
	private String baseUrl;

	/**
	 * The URL that the CAS server need to call back to finish the authentication process.
	 */
	private String callBackUrl;

}
