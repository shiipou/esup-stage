package fr.esupportail.esupstage.domain.jpa;

import fr.esupportail.esupstage.AbstractTest;
import fr.esupportail.esupstage.domain.jpa.entities.*;
import fr.esupportail.esupstage.domain.jpa.repositories.ContactRepository;
import fr.esupportail.esupstage.domain.jpa.repositories.ConventionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Rollback
@Transactional
class ConventionRepositoryTest extends AbstractTest {

		private final EntityManager entityManager;

		private final ConventionRepository conventionRepository;
		private int conventionId;

		@Autowired ConventionRepositoryTest(final EntityManager entityManager,
				final ConventionRepository conventionRepository) {
				super();
				this.entityManager = entityManager;
				this.conventionRepository = conventionRepository;
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

				TypeConvention typeConvention = new TypeConvention();
				typeConvention.setCodeCtrl("code");
				typeConvention.setLibelleTypeConvention("libel");
				typeConvention.setTemEnServTypeConvention("F");
				entityManager.persist(typeConvention);

				Theme theme = new Theme();
				theme.setLibelleTheme("libel");
				entityManager.persist(theme);

				TempsTravail tempsTravail = new TempsTravail();
				tempsTravail.setCodeCtrl("code");
				tempsTravail.setLibelleTempsTravail("libel");
				tempsTravail.setTemEnServTempsTravail("F");
				entityManager.persist(tempsTravail);

				NatureTravail natureTravail = new NatureTravail();
				natureTravail.setLibelleNatureTravail("libel");
				natureTravail.setTemEnServNatTrav("F");
				entityManager.persist(natureTravail);

				ModeValidationStage modeValidationStage = new ModeValidationStage();
				modeValidationStage.setLibelleModeValidationStage("libel");
				modeValidationStage.setTemEnServModeValid("F");
				entityManager.persist(modeValidationStage);

				LangueConvention langueConvention = new LangueConvention();
				langueConvention.setCodeLangueConvention("CD");
				langueConvention.setLibelleLangueConvention("libel");
				entityManager.persist(langueConvention);

				Indemnisation indemnisation = new Indemnisation();
				indemnisation.setLibelleIndemnisation("indem");
				indemnisation.setTemEnServIndem("F");
				entityManager.persist(indemnisation);

				Etudiant etudiant = new Etudiant();
				etudiant.setCodeUniversite("code");
				etudiant.setDateCreation(new Date(0));
				etudiant.setIdentEtudiant("ident");
				etudiant.setLoginCreation("login");
				etudiant.setNom("Name");
				etudiant.setNumEtudiant("125458");
				etudiant.setPrenom("Firstname");
				entityManager.persist(etudiant);

				Convention convention = new Convention();
				convention.setDateCreation(new Date(0));
				convention.setDateDebutStage(new Date(0));
				convention.setDateFinStage(new Date(0));
				convention.setDureeStage(100);
				convention.setIdAssurance(1);
				convention.setIdModeVersGratification(1);
				convention.setLoginCreation("login");
				convention.setNbJoursHebdo("1");
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


				this.conventionId = convention.getIdConvention();
				entityManager.flush();
		}


		@Test
		@DisplayName("findById – Nominal test case")
		void findById() {
				final Optional<Convention> result = conventionRepository.findById(this.conventionId);
				assertTrue(result.isPresent(), "We should have found our Convention");

				final Convention convention = result.get();
				assertEquals(new Date(0),convention.getDateCreation());
				assertEquals(new Date(0),convention.getDateDebutStage());
				assertEquals(new Date(0),convention.getDateFinStage());
				assertEquals(100,convention.getDureeStage());
				assertEquals(1,convention.getIdModeVersGratification());
				assertEquals(1,convention.getIdAssurance());
				assertEquals("login",convention.getLoginCreation());
				assertEquals("1",convention.getNbJoursHebdo());
				assertEquals("subject",convention.getSujetStage());
				assertEquals("s",convention.getTemConfSujetTeme());
				assertEquals("125458",convention.getEtudiant().getNumEtudiant());
				assertEquals("indem",convention.getIndemnisation().getLibelleIndemnisation());
				assertEquals("CD",convention.getLangueConvention().getCodeLangueConvention());
				assertEquals("libel",convention.getModeValidationStage().getLibelleModeValidationStage());
				assertEquals("libel",convention.getNatureTravail().getLibelleNatureTravail());
				assertEquals("libel",convention.getTempsTravail().getLibelleTempsTravail());
				assertEquals("libel",convention.getTheme().getLibelleTheme());
				assertEquals("libel",convention.getTypeConvention().getLibelleTypeConvention());
				assertEquals("login",convention.getCentreGestion().getLoginCreation());
		}


}
