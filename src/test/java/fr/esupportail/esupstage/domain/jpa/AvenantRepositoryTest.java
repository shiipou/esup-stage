package fr.esupportail.esupstage.domain.jpa;

import java.util.Date;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import fr.esupportail.esupstage.domain.jpa.entities.*;
import fr.esupportail.esupstage.domain.jpa.repositories.AccordPartenariatRepository;
import fr.esupportail.esupstage.domain.jpa.repositories.AffectationRepository;
import fr.esupportail.esupstage.domain.jpa.repositories.AssuranceRepository;
import fr.esupportail.esupstage.domain.jpa.repositories.AvenantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scripting.config.LangNamespaceHandler;
import org.springframework.test.annotation.Rollback;

import fr.esupportail.esupstage.AbstractTest;

import static org.junit.jupiter.api.Assertions.*;

@Rollback
@Transactional
class AvenantRepositoryTest extends AbstractTest {

		private final EntityManager entityManager;

		private final AvenantRepository avenantRepository;
		private int avenantId;

		@Autowired
		AvenantRepositoryTest(final EntityManager entityManager,
				final AvenantRepository avenantRepository) {
				super();
				this.entityManager = entityManager;
				this.avenantRepository = avenantRepository;
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



				Avenant avenant = new Avenant();
				avenant.setDateCreation(new Date(0));
				avenant.setInterruptionStage(false);
				avenant.setLoginCreation("login");
				avenant.setModificationEnseignant(false);
				avenant.setModificationMontantGratification(false);
				avenant.setModificationPeriode(false);
				avenant.setModificationSalarie(false);
				avenant.setModificationSujet(false);
				avenant.setRupture(true);
				avenant.setTitreAvenant("title");
				avenant.setConvention(convention);

				entityManager.persist(avenant);
				this.avenantId = avenant.getIdAvenant();
				entityManager.flush();
		}


		@Test
		@DisplayName("findById – Nominal test case")
		void findById() {
				final Optional<Avenant> result = avenantRepository.findById(this.avenantId);
				assertTrue(result.isPresent(), "We should have found our Assurance");

				final Avenant avenant = result.get();
				assertEquals("title",avenant.getTitreAvenant());
				assertEquals(new Date(0),avenant.getDateCreation());
				assertFalse(avenant.isInterruptionStage());
				assertFalse(avenant.isModificationEnseignant());
				assertTrue(avenant.isRupture());
				assertEquals("login",avenant.getConvention().getLoginCreation());
				assertEquals("125458",avenant.getConvention().getEtudiant().getNumEtudiant());
				assertEquals("indem",avenant.getConvention().getIndemnisation().getLibelleIndemnisation());
				assertEquals("CD",avenant.getConvention().getLangueConvention().getCodeLangueConvention());
				assertEquals("libel",avenant.getConvention().getModeValidationStage().getLibelleModeValidationStage());
				assertEquals("libel",avenant.getConvention().getNatureTravail().getLibelleNatureTravail());
				assertEquals("libel",avenant.getConvention().getTempsTravail().getLibelleTempsTravail());
				assertEquals("libel",avenant.getConvention().getTheme().getLibelleTheme());
				assertEquals("code",avenant.getConvention().getTypeConvention().getCodeCtrl());
				assertEquals("codeuniv",avenant.getConvention().getCentreGestion().getCodeUniversite());
				assertEquals("libel",avenant.getConvention().getCentreGestion().getConfidentialite().getLibelleConfidentialite());
				assertEquals("libel",avenant.getConvention().getCentreGestion().getNiveauCentre().getLibelleNiveauCentre());
		}

}