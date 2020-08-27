/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.esupportail.esupstage;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.jasig.cas.client.Protocol;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
 
@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
           
    @Value("${cas.url}")
    private String casUrl;
    
    @Value("${server.url}")
    private String serverUrl;
    
    @Value("${server.frontend.url}")
    private String serverFrontendHost;
    
    @Value("${server.servlet.context-path}")
    private String serverContextPath;
    
    // URL relative qui gère l'authentification coté application (login, global logout)
    @Value("${server.cas.authentication.url}")
    private String serverCasAuthenticationUrl;
    
    @Value("${server.cas.logout.url}")
    private String serverCasLogoutUrl;
    
    @Value("${server.switch.logout.url}")
    private String serverSwitchLogoutUrl;
    
    @Value("${server.switch.login.url}")
    private String serverSwitchLoginUrl;
    
    @Autowired
    private AuthenticationUserDetailsService loadUserDetailsService;

    @Autowired
    private UserDetailsService userDetailsService;
        
    /**
     * @return le filtre permettant à l'application de déconnecter l'utilisateur
     * en cas de requête de déconnexion globale provenant du CAS
     */
    @Bean
    public SingleSignOutFilter singleSignOutFilter() {
            SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();                        
            singleSignOutFilter.setCasServerUrlPrefix(casUrl);
            
            singleSignOutFilter.setIgnoreInitConfiguration(true);
            singleSignOutFilter.setArtifactParameterName(Protocol.CAS2.getArtifactParameterName());
            //singleSignOutFilter.setFrontLogoutParameterName(casUrl);
            singleSignOutFilter.setLogoutParameterName("logoutRequest");
            singleSignOutFilter.setRelayStateParameterName("RelayState");
            
            return singleSignOutFilter;
    }

    /**
     * @return le filtre permettant de lancer une requête de déconnexion globale au CAS
     * @throws UnsupportedEncodingException 
     */
    @Bean
    public LogoutFilter requestCasGlobalLogoutFilter() throws UnsupportedEncodingException {
            SecurityContextLogoutHandler handler = new SecurityContextLogoutHandler();
            //handler.setClearAuthentication(false);
            //handler.setInvalidateHttpSession(false);
            
            LogoutFilter logoutFilter = new LogoutFilter(casUrl + "/logout?service="
                            + URLEncoder.encode(serverUrl + serverContextPath + "/api/logout-success", "UTF-8"), handler);
            logoutFilter.setFilterProcessesUrl(serverCasLogoutUrl);
            logoutFilter.setLogoutRequestMatcher(new AntPathRequestMatcher(serverCasLogoutUrl, "GET"));
            return logoutFilter;
    }

    /**
     * Configuration de la partie sécurité (droits d'accès, ...)
     * @param http
     * @throws Exception 
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {        
        
        http.addFilter(casAuthenticationFilter());
        http.addFilterAfter(switchUserFilter(), FilterSecurityInterceptor.class);
       http.addFilterBefore(requestCasGlobalLogoutFilter(), LogoutFilter.class);
        http.addFilterBefore(singleSignOutFilter(), CasAuthenticationFilter.class);

        http.exceptionHandling()
                .authenticationEntryPoint(casAuthenticationEntryPoint());

        http.csrf()
                .ignoringAntMatchers(serverCasAuthenticationUrl)
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        
        // Spring security prend la première url qui match, l'ordre est important
        http.cors().and()
            .authorizeRequests()
                .antMatchers(serverSwitchLoginUrl).hasRole("SWITCH")
                .antMatchers(serverSwitchLogoutUrl).authenticated()
                .antMatchers("/api/login").authenticated()
                .antMatchers("/api/logout").authenticated()
                .antMatchers("/docs/**").authenticated()
                .antMatchers("/api/account").permitAll()
            .and()
            .authorizeRequests()
                .anyRequest()
                    .authenticated();
    }

    /**
     * Configuration de la gestion de la sécurité en fonction des urls
     * @param web
     */
    @Override
    public void configure(WebSecurity web) {
        web
            .ignoring()
            .antMatchers("/css/**","/js/**","/fonts/**","/images/**");
    }
    
    /**
     * 
     * @return les propriétés du service CAS
     */
    @Bean
    public ServiceProperties serviceProperties() {
        ServiceProperties serviceProperties = new ServiceProperties();
        serviceProperties.setService(serverUrl + serverContextPath + serverCasAuthenticationUrl);
        serviceProperties.setSendRenew(false);                
        return serviceProperties;
    }    
    
    /**     
     * @return le bean configuré du fournisseur d'authentification CAS
     */
    @Bean
    public CasAuthenticationProvider casAuthenticationProvider() {
        CasAuthenticationProvider casAuthenticationProvider = new CasAuthenticationProvider();
        casAuthenticationProvider.setAuthenticationUserDetailsService(loadUserDetailsService);
        casAuthenticationProvider.setServiceProperties(serviceProperties());
        casAuthenticationProvider.setTicketValidator(cas20ServiceTicketValidator());
        casAuthenticationProvider.setKey("CAS_DEFAULT_PROVIDER");
        return casAuthenticationProvider;
    }

    /**     
     * @return le service de validation de ticket
     */
    @Bean
    public Cas20ServiceTicketValidator cas20ServiceTicketValidator() {
        return new Cas20ServiceTicketValidator(casUrl);
    }

    /**     
     * @return le filtre d'authentification CAS
     * @throws Exception 
     */
    @Bean
    public CasAuthenticationFilter casAuthenticationFilter() throws Exception {
        CasAuthenticationFilter casAuthenticationFilter = new CasAuthenticationFilter();
        casAuthenticationFilter.setFilterProcessesUrl(serverCasAuthenticationUrl);
        casAuthenticationFilter.setAuthenticationManager(authenticationManager());
        return casAuthenticationFilter;
    }

    /**
     * @return le point d'entrée pour l'authentification avec CAS
     */
    @Bean
    public CasAuthenticationEntryPoint casAuthenticationEntryPoint() {
        CasAuthenticationEntryPoint casAuthenticationEntryPoint = new CasAuthenticationEntryPoint();
        casAuthenticationEntryPoint.setLoginUrl(casUrl+"/login");
        casAuthenticationEntryPoint.setServiceProperties(serviceProperties());
        return casAuthenticationEntryPoint;
    }

    /**
     * ajout de l'authentification CAS au gestionnaire d'authentification Spring security
     * @param auth
     * @throws Exception 
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(casAuthenticationProvider());
    }  
    
    /**
     * @return le filtre du switch d'utilisateur
     */
    @Bean
    public SwitchUserFilter switchUserFilter() {
        SwitchUserFilter filter = new SwitchUserFilter();
        filter.setUserDetailsService(userDetailsService);
        filter.setSwitchUserUrl(serverSwitchLoginUrl);
        filter.setExitUserUrl(serverSwitchLogoutUrl);
        filter.setFailureHandler(switchAuthenticationFailureHandler());
        filter.setSuccessHandler(switchAuthenticationSuccessHandler());
        return filter;
    }
    
    /**
     * @return le gestionnaire d'authentification rejeté
     */
    @Bean
    public AuthenticationFailureHandler switchAuthenticationFailureHandler() {
    	return new SwitchAuthenticationHandler(serverFrontendHost);
    }
    
    /**
     * @return le gestionnaire d'authentification accepté
     */
    @Bean
    public AuthenticationSuccessHandler switchAuthenticationSuccessHandler() {
    	return new SwitchAuthenticationHandler(serverFrontendHost);
    }
    
    /**
     * Gestion des CORS
     * @return la configuration des CORS
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(serverFrontendHost));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "HEAD", "PUT", "DELETE", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}