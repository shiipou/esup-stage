/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.esupportail.esupstage;

import fr.esupportail.esupstage.domain.LdapUser;
import fr.esupportail.esupstage.repositories.LdapUserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CasUserDetailsService implements AuthenticationUserDetailsService, UserDetailsService {

    @Autowired
    private LdapUserRepository ldapUserRepository;

    @Value("${ldap.group.switch}")
    private String groupSwitch;

    @Override
    public UserDetails loadUserDetails(Authentication token) throws UsernameNotFoundException {
        return getUserDetails(token.getName(), false);
    }

    /**
     * Utilisé pour le switch
     *
     * @param token
     * @return récupère les détails d'un utilisateur en fonction de l'identifiant fourni
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String token) throws UsernameNotFoundException {
        LdapUser user;
        // test du login alias
        try {
            user = ldapUserRepository.getUserByAliasLogin(token);
        } catch (Exception e) {
            // test de l'uid
            try {
                user = ldapUserRepository.getUser(token);
            } catch (Exception ex) {
                throw new UsernameNotFoundException(ex.getMessage());
            }
        }
        return getUserDetails(user.getLogin(), true);
    }

    /**
     * @param uid
     * @return les détails de l'utilisateur
     * @throws UsernameNotFoundException 
     */
    private UserDetails getUserDetails(String uid, boolean isSwitched) throws UsernameNotFoundException {
        List<GrantedAuthority> authorities = new ArrayList<>();

        log.info("Recuperation LdapUser uid = " + uid);

        LdapUser user = ldapUserRepository.getUser(uid);

        //Rôle switch
        if (user.getGroupes().contains(groupSwitch)) {
            authorities.add(new SimpleGrantedAuthority("ROLE_SWITCH"));
        }

        for (GrantedAuthority aut : authorities) {
            log.info(aut.toString());
        }

        return new UserInfos(uid, "password", user, authorities, isSwitched);
    }
}
