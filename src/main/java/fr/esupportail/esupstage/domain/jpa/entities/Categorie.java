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

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The persistent class for the Categorie database table.
 *
 */
@Entity
@Table(name = "Categorie")
@Getter
@Setter
@NoArgsConstructor
@NamedQuery(name = "Categorie.findAll", query = "SELECT c FROM Categorie c")
public class Categorie implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GenericGenerator(name = "HIBERNATE_SEQUENCE", strategy = "native")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "HIBERNATE_SEQUENCE")
    private Integer idCategorie;
    @Column(nullable = false)
    private Integer typeCategorie;
    // bi-directional many-to-one association to Critere
    @OneToMany(mappedBy = "categorie")
    private List<Critere> criteres;

    public Critere addCritere(Critere critere) {
        getCriteres().add(critere);
        critere.setCategorie(this);
        return critere;
    }

    public Critere removeCritere(Critere critere) {
        getCriteres().remove(critere);
        critere.setCategorie(null);
        return critere;
    }
}