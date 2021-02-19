package fr.esupportail.esupstage.domain.jpa.entities;


import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The persistent class for the ContratOffre database table.
 *
 */
@Entity
@Table(name = "ContratOffre")
@Getter
@Setter
@NoArgsConstructor
@NamedQuery(name = "ContratOffre.findAll", query = "SELECT c FROM ContratOffre c")
public class ContratOffre implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private Integer idContratOffre;
    @Column(nullable = false, length = 20)
    private String codeCtrl;
    @Column(nullable = false, length = 50)
    private String libelleContratOffre;
    private boolean modifiable;
    @Column(nullable = false, length = 1)
    private String temEnServContratOffre;
    // bi-directional many-to-one association to TypeOffre
    @ManyToOne
    @JoinColumn(name = "idTypeOffre", nullable = false)
    private TypeOffre typeOffre;
    // bi-directional many-to-one association to Offre
    @OneToMany(mappedBy = "contratOffre")
    private List<Offre> offres;

    public Integer getIdContratOffre() {
        return this.idContratOffre;
    }

    public Offre addOffre(Offre offre) {
        getOffres().add(offre);
        offre.setContratOffre(this);
        return offre;
    }

    public Offre removeOffre(Offre offre) {
        getOffres().remove(offre);
        offre.setContratOffre(null);
        return offre;
    }
}