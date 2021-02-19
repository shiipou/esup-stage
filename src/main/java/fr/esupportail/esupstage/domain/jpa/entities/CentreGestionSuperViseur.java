package fr.esupportail.esupstage.domain.jpa.entities;

import java.io.Serializable;
import java.util.ArrayList;
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
 * The persistent class for the CentreGestionSuperViseur database table.
 *
 */
@Entity
@Table(name = "CentreGestionSuperViseur")
@Getter
@Setter
@NamedQuery(name = "CentreGestionSuperViseur.findAll", query = "SELECT c FROM CentreGestionSuperViseur c")
public class CentreGestionSuperViseur implements Serializable {

    public CentreGestionSuperViseur() {
        this.centreGestions = new ArrayList<>();
    }

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private Integer idCentreGestionSuperViseur;
    @Column(nullable = false, length = 100)
    private String nomCentreSuperViseur;
    // bi-directional many-to-one association to CentreGestion
    @OneToMany(mappedBy = "centreGestionSuperViseur")
    private List<CentreGestion> centreGestions;

    public CentreGestion addCentreGestion(CentreGestion centreGestion) {
        getCentreGestions().add(centreGestion);
        centreGestion.setCentreGestionSuperViseur(this);
        return centreGestion;
    }

    public CentreGestion removeCentreGestion(CentreGestion centreGestion) {
        getCentreGestions().remove(centreGestion);
        centreGestion.setCentreGestionSuperViseur(null);
        return centreGestion;
    }
}