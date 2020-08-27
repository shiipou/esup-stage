/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.esupportail.esupstage.controllers;


import fr.esupportail.esupstage.UserInfos;
import fr.esupportail.esupstage.domain.dto.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import springfox.documentation.annotations.ApiIgnore;

/**
 *
 * @author david
 */
@Slf4j
@RestController
public class AccountController {
    
    @Value("${server.frontend.url}")
    private String urlFrontend;
    
    /**
     * Après connexion ou déconnexion, redirection vers la page d'accueil
     * @return l'url frontend
     */
    @ApiIgnore
    @RequestMapping(value={"/api/login", "/api/logout-success"})
    public RedirectView afterLoginLogout() {      
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(urlFrontend);
        return redirectView;
    }
    
    /**
     * 
     * @param auth
     * @return l'objet compte de l'utilisateur en session
     */
    @GetMapping("/api/account")
    public Account index(@ApiIgnore Authentication auth) {
       if(auth == null) {
           return new Account();
       }
       
       UserInfos userInfos = (UserInfos) auth.getPrincipal();  
    
        if(userInfos == null) {
            return new Account();
        }
        String name = auth.getName();
        String roles = auth.getAuthorities().toString();
        
        log.info (name + " - roles : " + roles);
        
        return userInfos.getAccount();
    }
}
