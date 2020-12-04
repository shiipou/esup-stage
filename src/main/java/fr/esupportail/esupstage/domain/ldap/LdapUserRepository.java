package fr.esupportail.esupstage.domain.ldap;

import java.util.Optional;

import org.springframework.data.ldap.repository.LdapRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LdapUserRepository extends LdapRepository<LdapUser> {

	Optional<LdapUser> findByLogin(String login);

	Optional<LdapUser> findByAliasLogin(String aliasLogin);

}
