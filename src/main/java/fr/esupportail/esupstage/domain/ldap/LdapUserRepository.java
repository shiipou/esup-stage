package fr.esupportail.esupstage.domain.ldap;

import java.util.Optional;

import org.springframework.data.ldap.repository.LdapRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LdapUserRepository extends LdapRepository<LdapUser> {

	Optional<LdapUser> findByLogin(String login);

	Optional<LdapUser> findByAliasLogin(String aliasLogin);

	// @Value("${ldap.attributs.id}")
	// private String idAttr;
	//
	// @Value("${ldap.attributs.login}")
	// private String loginAttr;
	//
	// @Value("${ldap.attributs}")
	// private String attributs;
	//
	// @Autowired
	// private LdapTemplate ldapTemplate;
	//
	// /**
	// * @param id
	// * @return l'utilisateur ldap correspondant à l'identifiant en paramètre
	// */
	// public LdapUser getUser(String id) {
	// return ldapTemplate.findOne(query().attributes(attributs.split(",")).where(idAttr).is(id), LdapUser.class);
	// }
	//
	// /**
	// * @param login
	// * @return l'utilisateur ldap correspondant au login en paramètre
	// */
	// public LdapUser getUserByAliasLogin(String login) {
	// return ldapTemplate.findOne(query().attributes(attributs.split(",")).where(loginAttr).is(login), LdapUser.class);
	// }
}
