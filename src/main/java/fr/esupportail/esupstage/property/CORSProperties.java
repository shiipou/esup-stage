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
public class CORSProperties {

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
