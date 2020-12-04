package fr.esupportail.esupstage.configuration.security;

/**
 * Enumeration of the different role available for an user.
 *
 * @author vdubus
 */
public enum UserRole {
	USER;

	public static UserRole findBy(final String name) {
		if (null == name) {
			throw new IllegalArgumentException();
		}
		final String value;
		if (name.startsWith("ROLE_")) {
			value = name.replaceFirst("ROLE_", "");
		} else {
			value = name;
		}
		return valueOf(value);
	}

	@Override
	public String toString() {
		return "ROLE_" + super.toString();
	}
}
