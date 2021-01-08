package fr.esupportail.esupstage.domain.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Calendar;
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
import fr.esupportail.esupstage.domain.jpa.entities.FicheEvaluation;
import fr.esupportail.esupstage.domain.jpa.entities.NiveauCentre;
import fr.esupportail.esupstage.domain.jpa.entities.QuestionSupplementaire;
import fr.esupportail.esupstage.domain.jpa.repositories.QuestionSupplementaireRepository;

@Rollback
@Transactional
public class QuestionSupplementaireRepositoryTest extends AbstractTest {

	private final EntityManager entityManager;

	private final QuestionSupplementaireRepository repository;

	private Integer id;

	@Autowired
	QuestionSupplementaireRepositoryTest(final EntityManager entityManager, final QuestionSupplementaireRepository repository) {
		super();
		this.entityManager = entityManager;
		this.repository = repository;
	}

	@BeforeEach
	void prepare() {
		final NiveauCentre niveauCentre = new NiveauCentre();
		niveauCentre.setLibelleNiveauCentre("Label");
		niveauCentre.setTemEnServNiveauCentre("A");
		entityManager.persist(niveauCentre);

		final Confidentialite confidentialite = new Confidentialite();
		confidentialite.setCodeConfidentialite("A");
		confidentialite.setLibelleConfidentialite("Label");
		confidentialite.setTemEnServConfid("A");
		entityManager.persist(confidentialite);

		final CentreGestion centreGestion = new CentreGestion();
		centreGestion.setAutorisationEtudiantCreationConvention(true);
		centreGestion.setCodeUniversite("CodeUniv");
		centreGestion.setDateCreation(Calendar.getInstance().getTime());
		centreGestion.setIdModeValidationStage(1);
		centreGestion.setLoginCreation("jdoe");
		centreGestion.setConfidentialite(confidentialite);
		centreGestion.setNiveauCentre(niveauCentre);
		entityManager.persist(centreGestion);

		final FicheEvaluation ficheEvaluation = new FicheEvaluation();
		ficheEvaluation.setCentreGestion(centreGestion);
		entityManager.persist(ficheEvaluation);

		final QuestionSupplementaire questionSupplementaire = new QuestionSupplementaire();
		questionSupplementaire.setIdPlacement(1);
		questionSupplementaire.setQuestion("Question");
		questionSupplementaire.setTypeQuestion("Type");
		questionSupplementaire.setFicheEvaluation(ficheEvaluation);
		entityManager.persist(questionSupplementaire);

		entityManager.flush();

		entityManager.refresh(questionSupplementaire);
		id = questionSupplementaire.getId();
	}

	@Test
	@DisplayName("findById – Nominal Test Case")
	void findById() {
		final Optional<QuestionSupplementaire> result = repository.findById(id);
		assertTrue(result.isPresent(), "We should have found our entity");

		final QuestionSupplementaire tmp = result.get();
		assertEquals(1, tmp.getIdPlacement());
		assertEquals("Question", tmp.getQuestion());
		assertEquals("Type", tmp.getTypeQuestion());
	}

}