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
package fr.esupportail.esupstage.configuration.security;

import static fr.esupportail.esupstage.configuration.security.UserRole.USER;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.stereotype.Component;

/**
 * @author vdubus
 */
@Component
public class CustomLdapAuthoritiesPopulator implements LdapAuthoritiesPopulator {

	@Autowired
	public CustomLdapAuthoritiesPopulator() {
		super();
	}

	@Override
	public Collection<? extends GrantedAuthority> getGrantedAuthorities(final DirContextOperations userData, final String login) {
		final Collection<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(USER.toString()));
		return authorities;
	}
}
