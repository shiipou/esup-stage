package fr.esupportail.esupstage.repositories;

import fr.esupportail.esupstage.domain.LdapUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.LdapTemplate;
import static org.springframework.ldap.query.LdapQueryBuilder.query;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class LdapUserRepository {

    @Value("${ldap.attributs.id}")
    private String idAttr;
    
    @Value("${ldap.attributs.login}")
    private String loginAttr;
    
    @Value("${ldap.attributs}")
    private String attributs;

    @Autowired
    private LdapTemplate ldapTemplate;
    
    /**
     * @param id
     * @return l'utilisateur ldap correspondant à l'identifiant en paramètre
     */
    public LdapUser getUser(String id) {
        return ldapTemplate.findOne(query().attributes(attributs.split(",")).where(idAttr).is(id), LdapUser.class);
    }
    
    /**
     * 
     * @param login
     * @return l'utilisateur ldap correspondant au login en paramètre
     */
    public LdapUser getUserByAliasLogin(String login) {
        return ldapTemplate.findOne(query().attributes(attributs.split(",")).where(loginAttr).is(login), LdapUser.class);
    }
}

