package fr.esupportail.esupstage.domain.jpa.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

/**
 * The persistent class for the Fichiers database table.
 *
 */
@Entity
@Table(name = "Fichiers")
@Getter
@Setter
@NoArgsConstructor
@NamedQuery(name = "Fichier.findAll", query = "SELECT f FROM Fichier f")
public class Fichier implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private Integer idFichier;
    @Column(nullable = false, length = 255)
    private String nomFichier;
    @Column(length = 255)
    private String nomReel;
    // bi-directional many-to-one association to CentreGestion
    @OneToMany(mappedBy = "fichier")
    private List<CentreGestion> centreGestions;
    // bi-directional many-to-one association to Offre
    @OneToMany(mappedBy = "fichier")
    private List<Offre> offres;

    public void setCentreGestions(List<CentreGestion> centreGestions) {
        this.centreGestions = centreGestions;
    }

    public CentreGestion addCentreGestion(CentreGestion centreGestion) {
        getCentreGestions().add(centreGestion);
        centreGestion.setFichier(this);
        return centreGestion;
    }

    public CentreGestion removeCentreGestion(CentreGestion centreGestion) {
        getCentreGestions().remove(centreGestion);
        centreGestion.setFichier(null);
        return centreGestion;
    }


}