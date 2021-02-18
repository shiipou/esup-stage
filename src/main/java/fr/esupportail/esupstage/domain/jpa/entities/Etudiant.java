package fr.esupportail.esupstage.domain.jpa.entities;

import java.util.Date;
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
 * The persistent class for the Etudiant database table.
 *
 */
@Entity
@Table(name = "Etudiant")
@Getter
@Setter
@NoArgsConstructor
@NamedQuery(name = "Etudiant.findAll", query = "SELECT e FROM Etudiant e")
public class Etudiant extends Auditable<String> {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private Integer idEtudiant;
    @Column(length = 1)
    private String codeSexe;
    @Column(nullable = false, length = 50)
    private String codeUniversite;
    private Date dateNais;
    @Column(nullable = false, length = 50)
    private String identEtudiant;
    @Column(length = 100)
    private String mail;
    @Column(nullable = false, length = 50)
    private String nom;
    @Column(length = 50)
    private String nomMarital;
    @Column(nullable = false, length = 20)
    private String numEtudiant;
    @Column(length = 15)
    private String numSS;
    @Column(nullable = false, length = 50)
    private String prenom;
    // bi-directional many-to-one association to Convention
    @OneToMany(mappedBy = "etudiant")
    private List<Convention> conventions;

    public Convention addConvention(Convention convention) {
        getConventions().add(convention);
        convention.setEtudiant(this);
        return convention;
    }

    public Convention removeConvention(Convention convention) {
        getConventions().remove(convention);
        convention.setEtudiant(null);
        return convention;
    }
}