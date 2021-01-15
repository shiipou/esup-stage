package fr.esupportail.esupstage.domain.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
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
import fr.esupportail.esupstage.domain.jpa.entities.CritereGestion;
import fr.esupportail.esupstage.domain.jpa.entities.CritereGestionPK;
import fr.esupportail.esupstage.domain.jpa.entities.NiveauCentre;
import fr.esupportail.esupstage.domain.jpa.repositories.CritereGestionRepository;

@Rollback
@Transactional
class CritereGestionRepositoryTest extends AbstractTest {

	private final EntityManager entityManager;

	private final CritereGestionRepository critereRepository;

	private CritereGestionPK critereGestionId;

	@Autowired
	CritereGestionRepositoryTest(final EntityManager entityManager, final CritereGestionRepository critereGestionRepository) {
		super();
		this.entityManager = entityManager;
		critereRepository = critereGestionRepository;
	}

	@BeforeEach
	void prepare() {
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

		final CritereGestionPK critereGestionPK = new CritereGestionPK();
		critereGestionPK.setCodeCritere("code");
		critereGestionPK.setCodeVersionEtape("code");

		final CritereGestion critereGestion = new CritereGestion();
		critereGestion.setId(critereGestionPK);
		critereGestion.setLibelleCritere("libel");
		critereGestion.setCentreGestion(centreGestion);
		entityManager.persist(critereGestion);

		critereGestionId = critereGestion.getId();
		entityManager.flush();
	}

	@Test
	@DisplayName("findById – Nominal test case")
	void findById() {
		final Optional<CritereGestion> result = critereRepository.findById(critereGestionId);
		assertTrue(result.isPresent(), "We should have found our CritereGestion");

		final CritereGestion critereGestion = result.get();
		assertEquals("libel", critereGestion.getLibelleCritere());
		assertEquals("login", critereGestion.getCentreGestion().getLoginCreation());
	}

}