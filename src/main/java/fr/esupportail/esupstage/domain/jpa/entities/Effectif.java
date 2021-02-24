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
 * The persistent class for the Effectif database table.
 *
 */
@Entity
@Table(name = "Effectif")
@Getter
@Setter
@NoArgsConstructor
@NamedQuery(name = "Effectif.findAll", query = "SELECT e FROM Effectif e")
public class Effectif implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GenericGenerator(name = "HIBERNATE_SEQUENCE", strategy = "native")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "HIBERNATE_SEQUENCE")
    private Integer idEffectif;
    @Column(nullable = false, length = 100)
    private String libelleEffectif;
    private boolean modifiable;
    @Column(nullable = false, length = 1)
    private String temEnServEffectif;
    // bi-directional many-to-one association to Structure
    @OneToMany(mappedBy = "effectif")
    private List<Structure> structures;

    public Structure addStructure(Structure structure) {
        getStructures().add(structure);
        structure.setEffectif(this);
        return structure;
    }

    public Structure removeStructure(Structure structure) {
        getStructures().remove(structure);
        structure.setEffectif(null);
        return structure;
    }
}
