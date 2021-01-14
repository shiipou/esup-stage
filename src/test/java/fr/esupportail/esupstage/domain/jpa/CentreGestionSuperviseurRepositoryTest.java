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
class CentreGestionSuperviseurRepositoryTest extends AbstractTest {

		private final EntityManager entityManager;

		private final CentreGestionSuperViseurRepository centreGestionSuperViseurRepository;
		private int centreGestionSuperviseurId;

		@Autowired CentreGestionSuperviseurRepositoryTest(final EntityManager entityManager,
				final CentreGestionSuperViseurRepository centreGestionSuperViseurRepository) {
				super();
				this.entityManager = entityManager;
				this.centreGestionSuperViseurRepository= centreGestionSuperViseurRepository;
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

				CentreGestionSuperViseur centreGestionSuperViseur = new CentreGestionSuperViseur();
				centreGestionSuperViseur.setNomCentreSuperViseur("name");
				centreGestionSuperViseur.addCentreGestion(centreGestion);
				entityManager.persist(centreGestionSuperViseur);

				this.centreGestionSuperviseurId= centreGestionSuperViseur.getIdCentreGestionSuperViseur();
				entityManager.flush();
		}


		@Test
		@DisplayName("findById – Nominal test case")
		void findById() {
				final Optional<CentreGestionSuperViseur> result = centreGestionSuperViseurRepository.findById(this.centreGestionSuperviseurId);
				assertTrue(result.isPresent(), "We should have found our CentreGestionSuperviseur");

				final CentreGestionSuperViseur centreGestionSuperViseur = result.get();
				assertEquals("codeuniv",centreGestionSuperViseur.getCentreGestions().get(0).getCodeUniversite());
				assertEquals("name",centreGestionSuperViseur.getNomCentreSuperViseur());
		}

}
