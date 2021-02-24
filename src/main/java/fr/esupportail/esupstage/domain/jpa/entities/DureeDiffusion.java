package fr.esupportail.esupstage.domain.jpa.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The persistent class for the DureeDiffusion database table.
 *
 */
@Entity
@Table(name = "DureeDiffusion")
@Getter
@Setter
@NoArgsConstructor
@NamedQuery(name = "DureeDiffusion.findAll", query = "SELECT d FROM DureeDiffusion d")
public class DureeDiffusion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GenericGenerator(name = "HIBERNATE_SEQUENCE", strategy = "native")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "HIBERNATE_SEQUENCE")
    private Integer idDureeDiffusion;
    @Column(nullable = false)
    private boolean adminSeulement;
    @Column(nullable = false, length = 20)
    private String libelleDureeDiffusion;

}