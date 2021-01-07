package fr.esupportail.esupstage.domain.jpa.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the OffreDiffusion database table.
 *
 */
@Entity
@Table(name = "OffreDiffusion")
@Getter
@Setter
@NoArgsConstructor
@NamedQuery(name = "OffreDiffusion.findAll", query = "SELECT o FROM OffreDiffusion o")
public class OffreDiffusion implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    private OffreDiffusionPK id;
    @Column(nullable = false)
    private boolean estMiseEnAvant;
    // bi-directional many-to-one association to CentreGestion
    @ManyToOne
    @JoinColumn(name = "idCentreGestion", nullable = false, insertable = false, updatable = false)
    private CentreGestion centreGestion;
    // bi-directional many-to-one association to Offre
    @ManyToOne
    @JoinColumn(name = "idOffre", nullable = false, insertable = false, updatable = false)
    private Offre offre;
}