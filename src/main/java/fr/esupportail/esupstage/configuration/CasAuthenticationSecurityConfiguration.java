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
package fr.esupportail.esupstage.configuration;

import java.util.Arrays;

import javax.servlet.http.HttpSessionEvent;

import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.validation.Cas30ServiceTicketValidator;
import org.jasig.cas.client.validation.TicketValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import fr.esupportail.esupstage.configuration.security.CustomCasAuthenticationEntryPoint;
import fr.esupportail.esupstage.configuration.security.CustomUserDetailsService;
import fr.esupportail.esupstage.property.ApplicationProperties;

/**
 * @author vdubus
 */
@Configuration
@Profile("cas")
public class CasAuthenticationSecurityConfiguration extends WebSecurityConfigurerAdapter {

	private final CustomUserDetailsService authenticationService;

	private final ApplicationProperties applicationProperties;

	public CasAuthenticationSecurityConfiguration(final CustomUserDetailsService authenticationService, final ApplicationProperties applicationProperties) {
		this.authenticationService = authenticationService;
		this.applicationProperties = applicationProperties;
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		// @formatter:off
		http.authorizeRequests().antMatchers("/swagger-ui.html", "/static/**").permitAll();
		http.authorizeRequests().antMatchers("/login").authenticated()
				.and().httpBasic().authenticationEntryPoint(this.authenticationEntryPoint())
				.and().logout().logoutUrl("/logout").logoutSuccessUrl("/logout/cas")
				.and().addFilterBefore(this.singleSignOutFilter(), CasAuthenticationFilter.class).addFilterBefore(this.logoutFilter(), LogoutFilter.class)
		;
		// @formatter:on
		if (this.applicationProperties.getCsrf().isEnable()) {
			final CookieCsrfTokenRepository csrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
			// It seems that as we don't deploy the front on the same path as the back,
			// we need to set the CSRF Token cookie on the root of the domain used by both applications.
			csrfTokenRepository.setCookiePath(this.applicationProperties.getCsrf().getContext());
			http.csrf().csrfTokenRepository(csrfTokenRepository);
		} else {
			http.csrf().disable();
		}
		http.cors();
	}

	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		final CustomCasAuthenticationEntryPoint entryPoint = new CustomCasAuthenticationEntryPoint();
		entryPoint.setLoginUrl(this.applicationProperties.getCas().getBaseUrl() + this.applicationProperties.getCas().getLoginEndPoint());
		entryPoint.setServiceProperties(this.serviceProperties());
		return entryPoint;
	}

	@Override
	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(this.casAuthenticationProvider());
	}

	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return new ProviderManager(Arrays.asList(this.casAuthenticationProvider()));
	}

	@Bean
	public CasAuthenticationFilter casAuthenticationFilter(final ServiceProperties sP) throws Exception {
		final CasAuthenticationFilter filter = new CasAuthenticationFilter();
		filter.setServiceProperties(sP);
		filter.setAuthenticationManager(this.authenticationManager());
		return filter;
	}

	@Bean
	public ServiceProperties serviceProperties() {
		final ServiceProperties serviceProperties = new ServiceProperties();
		serviceProperties.setService(this.applicationProperties.getCas().getCallBackUrl() + "/login/cas");
		serviceProperties.setSendRenew(false);
		return serviceProperties;
	}

	@Bean
	public TicketValidator ticketValidator() {
		return new Cas30ServiceTicketValidator(this.applicationProperties.getCas().getBaseUrl());
	}

	@Bean
	public CasAuthenticationProvider casAuthenticationProvider() {
		final CasAuthenticationProvider provider = new CasAuthenticationProvider();
		provider.setServiceProperties(this.serviceProperties());
		provider.setTicketValidator(this.ticketValidator());
		provider.setAuthenticationUserDetailsService(this.authenticationService);
		provider.setKey("casVTws");
		return provider;
	}

	@Bean
	public SecurityContextLogoutHandler securityContextLogoutHandler() {
		return new SecurityContextLogoutHandler();
	}

	@Bean
	public LogoutFilter logoutFilter() {
		final LogoutFilter logoutFilter = new LogoutFilter(this.applicationProperties.getCas().getBaseUrl() + this.applicationProperties.getCas().getLogoutEndPoint(),
				this.securityContextLogoutHandler());
		logoutFilter.setFilterProcessesUrl("/logout/cas");
		return logoutFilter;
	}

	@Bean
	public SingleSignOutFilter singleSignOutFilter() {
		final SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
		// singleSignOutFilter.setCasServerUrlPrefix(this.casProperties.getBaseUrl());
		singleSignOutFilter.setIgnoreInitConfiguration(true);
		return singleSignOutFilter;
	}

	@EventListener
	public SingleSignOutHttpSessionListener singleSignOutHttpSessionListener(final HttpSessionEvent event) {
		return new SingleSignOutHttpSessionListener();
	}

}
