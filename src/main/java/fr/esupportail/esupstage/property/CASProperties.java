/*******************************************************************************
 * Copyright 2018 UPHF
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
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
