package fr.esupportail.esupstage.domain.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import fr.esupportail.esupstage.AbstractTest;
import fr.esupportail.esupstage.domain.jpa.entities.CentreGestion;
import fr.esupportail.esupstage.domain.jpa.entities.Confidentialite;
import fr.esupportail.esupstage.domain.jpa.entities.Convention;
import fr.esupportail.esupstage.domain.jpa.entities.Etudiant;
import fr.esupportail.esupstage.domain.jpa.entities.Indemnisation;
import fr.esupportail.esupstage.domain.jpa.entities.LangueConvention;
import fr.esupportail.esupstage.domain.jpa.entities.ModeValidationStage;
import fr.esupportail.esupstage.domain.jpa.entities.NatureTravail;
import fr.esupportail.esupstage.domain.jpa.entities.NbJourHebdo;
import fr.esupportail.esupstage.domain.jpa.entities.NiveauCentre;
import fr.esupportail.esupstage.domain.jpa.entities.TempsTravail;
import fr.esupportail.esupstage.domain.jpa.entities.Theme;
import fr.esupportail.esupstage.domain.jpa.entities.TypeConvention;
import fr.esupportail.esupstage.domain.jpa.repositories.ModeValidationStageRepository;

@Rollback
@Transactional
class ModeValidationStageRepositoryTest extends AbstractTest {

	private final EntityManager entityManager;

	private final ModeValidationStageRepository modeValidationStageRepository;

	private int lastInsertedId;

	@Autowired
	ModeValidationStageRepositoryTest(final EntityManager entityManager, final ModeValidationStageRepository modeValidationStageRepository) {
		super();
		this.entityManager = entityManager;
		this.modeValidationStageRepository = modeValidationStageRepository;
	}

	@BeforeEach
	void prepare() {
		ModeValidationStage modeValidationStage = new ModeValidationStage();
		modeValidationStage.setLabel("libelleModeValidationStage");
		modeValidationStage.setModifiable(true);
		modeValidationStage.setTemEnServ("A");
		entityManager.persist(modeValidationStage);

		final Indemnisation indemnisation = new Indemnisation();
		indemnisation.setLabel("libelleIndemnisation");
		indemnisation.setTemEnServ("A");

		entityManager.persist(indemnisation);

		final NiveauCentre niveauCentre = new NiveauCentre();
		niveauCentre.setLabel("libel");
		niveauCentre.setTemEnServ("A");

		entityManager.persist(niveauCentre);

		final Confidentialite confidentialite = new Confidentialite();
		confidentialite.setCode("A");
		confidentialite.setLabel("libel");
		confidentialite.setTemEnServ("A");
		entityManager.persist(confidentialite);

		final CentreGestion centreGestion = new CentreGestion();
		centreGestion.setAutorisationEtudiantCreationConvention(true);
		centreGestion.setCodeUniversite("codeuniv");
		centreGestion.setIdModeValidationStage(1);
		centreGestion.setCreatedBy("login");
		centreGestion.setConfidentialite(confidentialite);
		centreGestion.setNiveauCentre(niveauCentre);
		entityManager.persist(centreGestion);

		TypeConvention typeConvention = new TypeConvention();
		typeConvention.setCodeCtrl("code");
		typeConvention.setLabel("libel");
		typeConvention.setTemEnServ("F");
		entityManager.persist(typeConvention);

		Theme theme = new Theme();
		theme.setLabel("libel");
		entityManager.persist(theme);

		TempsTravail tempsTravail = new TempsTravail();
		tempsTravail.setCodeCtrl("code");
		tempsTravail.setLabel("libel");
		tempsTravail.setTemEnServ("F");
		entityManager.persist(tempsTravail);

		NatureTravail natureTravail = new NatureTravail();
		natureTravail.setLabel("libel");
		natureTravail.setTemEnServ("F");
		entityManager.persist(natureTravail);

		LangueConvention langueConvention = new LangueConvention();
		langueConvention.setCode("CD");
		langueConvention.setLabel("libel");
		entityManager.persist(langueConvention);

		Etudiant etudiant = new Etudiant();
		etudiant.setCodeUniversite("code");
		etudiant.setCreatedDate(LocalDateTime.now());
		etudiant.setIdentEtudiant("ident");
		etudiant.setCreatedBy("login");
		etudiant.setNom("Name");
		etudiant.setNumEtudiant("125458");
		etudiant.setPrenom("Firstname");
		entityManager.persist(etudiant);

		Convention convention = new Convention();
		convention.setDateDebutStage(LocalDate.now());
		convention.setDateFinStage(LocalDate.now());
		convention.setDureeStage(100);
		convention.setIdAssurance(1);
		convention.setIdModeVersGratification(1);
		convention.setCreatedBy("login");
		convention.setNbJoursHebdo(NbJourHebdo.NB_JOURS_1_0);
		convention.setSujetStage("subject");
		convention.setTemConfSujetTeme("s");
		convention.setEtudiant(etudiant);
		convention.setIndemnisation(indemnisation);
		convention.setLangueConvention(langueConvention);
		convention.setModeValidationStage(modeValidationStage);
		convention.setNatureTravail(natureTravail);
		convention.setTempsTravail(tempsTravail);
		convention.setTheme(theme);
		convention.setTypeConvention(typeConvention);
		convention.setCentreGestion(centreGestion);
		entityManager.persist(convention);

		this.entityManager.flush();

		this.entityManager.refresh(modeValidationStage);
		this.lastInsertedId = modeValidationStage.getId();
	}

	private void testModeValidationStageFields(int indice, ModeValidationStage modeValidationStage) {
		switch (indice) {
		case 0:
			assertEquals(this.lastInsertedId, modeValidationStage.getId(), "ModeValidationStage id match");
			assertEquals("libelleModeValidationStage", modeValidationStage.getLabel(), "ModeValidationStage libelle match");
			assertEquals("A", modeValidationStage.getTemEnServ(), "ModeValidationStage temEnServ match");
			assertEquals("subject", modeValidationStage.getConventions().get(0).getSujetStage(), "ModeValidationStage temEnServ match");
			break;
		}
	}

	@Test
	@DisplayName("findById – Nominal test case")
	void findById() {
		final Optional<ModeValidationStage> result = this.modeValidationStageRepository.findById(this.lastInsertedId);
		assertTrue(result.isPresent(), "We should have found our ModeValidationStage");

		final ModeValidationStage modeCandidature = result.get();
		this.testModeValidationStageFields(0, modeCandidature);
	}

	@Test
	@DisplayName("findAll – Nominal test case")
	void findAll() {
		final List<ModeValidationStage> result = this.modeValidationStageRepository.findAll();
		assertEquals(1, result.size(), "We should have found our ModeValidationStage");

		final ModeValidationStage modeCandidature = result.get(0);
		assertNotNull(modeCandidature, "ModeValidationStage exist");
		this.testModeValidationStageFields(0, modeCandidature);
	}

}
