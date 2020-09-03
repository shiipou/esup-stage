/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.esupportail.esupstage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Profile("!prod")
public class SwaggerConfiguration extends WebMvcConfigurationSupport {

    /**
     * Construction de swagger
     * @return 
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("fr.esupportail.esupstage.controllers"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * Redirection des urls d'origine de swagger
     * @param registry 
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/docs/v2/api-docs", "/v2/api-docs").setKeepQueryParams(true);
        registry.addRedirectViewController("/docs/swagger-resources/configuration/ui", "/swagger-resources/configuration/ui");
        registry.addRedirectViewController("/docs/swagger-resources/configuration/security", "/swagger-resources/configuration/security");
        registry.addRedirectViewController("/docs/swagger-resources", "/swagger-resources");
    }

    /**
     * Configuration du path de swagger
     * @param registry 
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //TODO : Test for sonarcloud
        registry.addResourceHandler("/docs/**").addResourceLocations("classpath:/META-INF/resources/");
    }
}
