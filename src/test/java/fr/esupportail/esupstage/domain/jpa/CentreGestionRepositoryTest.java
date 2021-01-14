package fr.esupportail.esupstage.domain.jpa;

import java.util.Date;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import fr.esupportail.esupstage.domain.jpa.entities.*;
import fr.esupportail.esupstage.domain.jpa.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import fr.esupportail.esupstage.AbstractTest;

import static org.junit.jupiter.api.Assertions.*;

@Rollback
@Transactional
class CentreGestionRepositoryTest extends AbstractTest {

		private final EntityManager entityManager;

		private final CentreGestionRepository centreGestionRepository;
		private int centreGestionId;

		@Autowired
		CentreGestionRepositoryTest(final EntityManager entityManager,
				final CentreGestionRepository centreGestionRepository) {
				super();
				this.entityManager = entityManager;
				this.centreGestionRepository = centreGestionRepository;
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
				centreGestion.setDateCreation(new Date(0));
				centreGestion.setIdModeValidationStage(1);
				centreGestion.setLoginCreation("login");
				centreGestion.setConfidentialite(confidentialite);
				centreGestion.setNiveauCentre(niveauCentre);
				entityManager.persist(centreGestion);

				entityManager.persist(centreGestion);
				this.centreGestionId = centreGestion.getIdCentreGestion();
				entityManager.flush();
		}


		@Test
		@DisplayName("findById – Nominal test case")
		void findById() {
				final Optional<CentreGestion> result = centreGestionRepository.findById(this.centreGestionId);
				assertTrue(result.isPresent(), "We should have found our CentreGestion");

				final CentreGestion centreGestion = result.get();
				assertTrue(centreGestion.isAutorisationEtudiantCreationConvention());
				assertEquals("codeuniv",centreGestion.getCodeUniversite());
				assertEquals(new Date(0),centreGestion.getDateCreation());
				assertEquals(1,centreGestion.getIdModeValidationStage());
				assertEquals("login",centreGestion.getLoginCreation());
				assertEquals("libel",centreGestion.getConfidentialite().getLibelleConfidentialite());
				assertEquals("libel",centreGestion.getNiveauCentre().getLibelleNiveauCentre());
		}

}
