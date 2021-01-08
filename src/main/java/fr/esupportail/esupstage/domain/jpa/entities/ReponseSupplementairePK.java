package fr.esupportail.esupstage.domain.jpa.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The primary key class for the ReponseSupplementaire database table.
 *
 */
@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class ReponseSupplementairePK implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(insertable = false, updatable = false, unique = true, nullable = false)
	private Integer idQuestionSupplementaire;

	@Column(insertable = false, updatable = false, unique = true, nullable = false)
	private Integer idConvention;

}