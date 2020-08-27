/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.esupportail.esupstage;
 
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
 
@Configuration
@EnableConfigurationProperties
@ComponentScan(basePackages={"fr.esupportail.esupstage", "fr.esupportail.utils"})
@EnableTransactionManagement
@EnableCaching
public class MVCConfiguration {
               
    /**
     * 
     * @param contextSource
     * @return le service d'accès ldap
     */
    @Bean
    public LdapTemplate ldapTemplate(ContextSource contextSource) {
        LdapContextSource src = (LdapContextSource) contextSource;
        return new LdapTemplate(contextSource);
    }

    /**
     * 
     * @return le contexte source ldap
     */
    @Bean
    @ConfigurationProperties(prefix="ldap")
    public LdapContextSource contextSource() {
        LdapContextSource contextSource = new LdapContextSource();
        return contextSource;
    }
}
