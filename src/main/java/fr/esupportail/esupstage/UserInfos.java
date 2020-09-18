/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.esupportail.esupstage;

import java.util.ArrayList;

import fr.esupportail.esupstage.domain.dto.Account;
import fr.esupportail.esupstage.domain.ldap.LdapUser;

import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * Informations de l'utilisateur
 * @author david
 */

@Getter
@Setter
public class UserInfos extends User {
    
    private Account account;
    
    /**
     * @param id
     * @param password
     * @param authorities
     * @param user
     */
    public UserInfos(String id, String password, LdapUser user, Collection<? extends GrantedAuthority> authorities, boolean switched) {
        super(id, password, authorities);
        List<String> roles = new ArrayList<>();
        for(GrantedAuthority authority : authorities) {
            roles.add(authority.getAuthority());
        }
        //TODO : Test for sonarcloud
        this.account = new Account(user.getPrenom() + " " + user.getNom(), roles, switched);
    }
}
