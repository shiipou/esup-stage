package fr.esupportail.esupstage.domain.jpa;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import fr.esupportail.esupstage.domain.jpa.entities.*;
import fr.esupportail.esupstage.domain.jpa.repositories.AccordPartenariatRepository;
import org.eclipse.jdt.internal.compiler.env.IModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import fr.esupportail.esupstage.AbstractTest;

import static org.junit.jupiter.api.Assertions.*;

@Rollback
@Transactional
class AccordPartenariatRepositoryTest extends AbstractTest {

    private final EntityManager entityManager;

    private final AccordPartenariatRepository accordPartenariatRepository;
    private int accordPartenariatId;

    @Autowired
    AccordPartenariatRepositoryTest(final EntityManager entityManager,
                                    final AccordPartenariatRepository accordPartenariatRepository) {
        super();
        this.entityManager = entityManager;
        this.accordPartenariatRepository = accordPartenariatRepository;
    }

    @BeforeEach
    void prepare() {

        final AccordPartenariat accordPartenariat = new AccordPartenariat();
        final NiveauCentre niveauCentre = new NiveauCentre();
        niveauCentre.setLibelleNiveauCentre("libel");
        niveauCentre.setTemEnServNiveauCentre("A");

        entityManager.persist(niveauCentre);

        final Confidentialite confidentialite = new Confidentialite();
        confidentialite.setCodeConfidentialite("A");
        confidentialite.setLibelleConfidentialite("libel");
        confidentialite.setTemEnServConfid("A");
        entityManager.persist(confidentialite);

        final CentreGestion centreGestion = new CentreGestion();
        centreGestion.setAutorisationEtudiantCreationConvention(true);
        centreGestion.setCodeUniversite("codeuniv");
        centreGestion.setDateCreation(new Date());
        centreGestion.setIdModeValidationStage(1);
        centreGestion.setLoginCreation("login");
        centreGestion.setConfidentialite(confidentialite);
        centreGestion.setNiveauCentre(niveauCentre);
        entityManager.persist(centreGestion);

        final Pays pays = new Pays();
        pays.setActual(1);
        pays.setLib("lib");
        pays.setTemEnServPays("A");
        pays.setCog(1);
        entityManager.persist(pays);

        final Effectif effectif = new Effectif();
        effectif.setLibelleEffectif("libel");
        effectif.setTemEnServEffectif("A");
        entityManager.persist(effectif);

        final TypeStructure typeStructure = new TypeStructure();
        typeStructure.setLibelleTypeStructure("libel");
        typeStructure.setTemEnServTypeStructure("A");
        entityManager.persist(typeStructure);

        final Structure structure = new Structure();
        structure.setDateCreation(new Date());
        structure.setEstValidee(1);
        structure.setLoginCreation("login");
        structure.setRaisonSociale("raison");
        structure.setVoie("voie");
        structure.setEffectif(effectif);
        structure.setPay(pays);
        structure.setTypeStructure(typeStructure);
        entityManager.persist(structure);

        final Service service = new Service();
        service.setCodePostal("17000");
        service.setNom("nom service");
        service.setPay(pays);
        service.setStructure(structure);
        service.setVoie("voie");
        entityManager.persist(service);


        final Contact contact = new Contact();
        contact.setDateCreation(new Date());
        contact.setFonction("fonction");
        contact.setLogin("login");
        contact.setNom("Doe");
        contact.setPrenom("John");
        contact.setLoginCreation("login");
        contact.setCentreGestion(centreGestion);
        contact.setService(service);
        entityManager.persist(contact);


        accordPartenariat.setContact(contact);
        accordPartenariat.setStructure(structure);
        accordPartenariat.setDateCreation(new Date());
        accordPartenariat.setLoginCreation("login");



        entityManager.persist(accordPartenariat);
        this.accordPartenariatId = accordPartenariat.getIdAccordPartenariat();
        entityManager.flush();
    }

    @Test
    @DisplayName("findById – Nominal test case")
    void findById() {
        final Optional<AccordPartenariat> result = accordPartenariatRepository.findById(this.accordPartenariatId);
        assertTrue(result.isPresent(), "We should have found our teacher");

        final AccordPartenariat accordPartenariat = result.get();
        assertEquals("login",accordPartenariat.getLoginCreation());
        assertEquals(this.accordPartenariatId,accordPartenariat.getIdAccordPartenariat());
        assertEquals("Doe",accordPartenariat.getContact().getNom());
        assertEquals("nom service",accordPartenariat.getContact().getService().getNom());
        assertEquals("voie",accordPartenariat.getContact().getService().getStructure().getVoie());
        assertEquals("libel",accordPartenariat.getContact().getService().getStructure().
                getTypeStructure().getLibelleTypeStructure());
        assertEquals("libel",accordPartenariat.getContact().getService().getStructure().
                getEffectif().getLibelleEffectif());
        assertEquals("lib",accordPartenariat.getContact().getService().getStructure().getPay().getLib());
        assertEquals("codeuniv",accordPartenariat.getContact().getCentreGestion().getCodeUniversite());
        assertEquals("libel",accordPartenariat.getContact().getCentreGestion().
                getConfidentialite().getLibelleConfidentialite());
    }

}
