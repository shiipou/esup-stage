package fr.esupportail.esupstage.domain.jpa.entities;

import java.io.Serializable;
import java.util.LinkedList;
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
import lombok.Setter;

/**
 * The persistent class for the OrigineStage database table.
 *
 */
@Entity
@Getter
@Setter
@Table(name = "OrigineStage")
@NamedQuery(name = "OrigineStage.findAll", query = "SELECT o FROM OrigineStage o")
public class OrigineStage implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	private Integer idOrigineStage;

	@Column(nullable = false, length = 45)
	private String libelleOrigineStage;

	private boolean modifiable;

	@Column(nullable = false, length = 1)
	private String temEnServOrigineStage;

	// bi-directional many-to-one association to Convention
	@OneToMany(mappedBy = "origineStage")
	private List<Convention> conventions;

	public OrigineStage() {
		super();
		this.conventions = new LinkedList<>();
	}

	public Convention addConvention(Convention convention) {
		getConventions().add(convention);
		convention.setOrigineStage(this);
		return convention;
	}

	public Convention removeConvention(Convention convention) {
		getConventions().remove(convention);
		convention.setOrigineStage(null);
		return convention;
	}

}