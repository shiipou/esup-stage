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
 * The persistent class for the NiveauCentre database table.
 *
 */
@Entity
@Table(name = "NiveauCentre")
@Getter
@Setter
@NoArgsConstructor
@NamedQuery(name = "NiveauCentre.findAll", query = "SELECT n FROM NiveauCentre n")
public class NiveauCentre implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private Integer idNiveauCentre;
    @Column(nullable = false, length = 50)
    private String libelleNiveauCentre;
    @Column(nullable = false, length = 1)
    private String temEnServNiveauCentre;
    // bi-directional many-to-one association to CentreGestion
    @OneToMany(mappedBy = "niveauCentre")
    private List<CentreGestion> centreGestions;

    public CentreGestion addCentreGestion(CentreGestion centreGestion) {
        getCentreGestions().add(centreGestion);
        centreGestion.setNiveauCentre(this);
        return centreGestion;
    }

    public CentreGestion removeCentreGestion(CentreGestion centreGestion) {
        getCentreGestions().remove(centreGestion);
        centreGestion.setNiveauCentre(null);
        return centreGestion;
    }
}