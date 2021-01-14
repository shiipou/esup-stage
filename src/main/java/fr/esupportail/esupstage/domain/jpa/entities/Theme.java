package fr.esupportail.esupstage.domain.jpa.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The persistent class for the Theme database table.
 *
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Theme")
@NamedQuery(name = "Theme.findAll", query = "SELECT t FROM Theme t")
public class Theme implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	private Integer idTheme;

	@Column(nullable = false, length = 50)
	private String libelleTheme;

	@Column(length = 1)
	private String temEnServTheme;

	@OneToMany(mappedBy = "theme")
	private List<Convention> conventions;

	public Convention addConvention(Convention convention) {
		getConventions().add(convention);
		convention.setTheme(this);
		return convention;
	}

	public Convention removeConvention(Convention convention) {
		getConventions().remove(convention);
		convention.setTheme(null);
		return convention;
	}

}