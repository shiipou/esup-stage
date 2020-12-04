package fr.esupportail.esupstage.property;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CSRFProperties implements Serializable {

	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Enable / disable CSRF protection.<br>
	 * default: {@code true}
	 */
	private boolean enable = true;

	/**
	 * Define the context path of the CSRF cookie.<br>
	 * Default: {@code "/"}
	 */
	private String context = "/";

}
