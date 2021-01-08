package fr.esupportail.esupstage.configuration.jpa;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Implementation of {@link AuditorAware} using the authentication information.
 *
 * @author vdubus
 */
public class AuthenticationAuditorAware implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		final Optional<String> result;
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated() || !UserDetails.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
			result = Optional.empty();
		} else {
			result = Optional.of(((UserDetails) authentication.getPrincipal()).getUsername());
		}
		return result;
	}

}
