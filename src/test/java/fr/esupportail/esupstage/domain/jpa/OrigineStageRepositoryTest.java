package fr.esupportail.esupstage.domain.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import fr.esupportail.esupstage.AbstractTest;
import fr.esupportail.esupstage.domain.jpa.entities.OrigineStage;
import fr.esupportail.esupstage.domain.jpa.repositories.OrigineStageRepository;

@Rollback
@Transactional
public class OrigineStageRepositoryTest extends AbstractTest {

	private final EntityManager entityManager;

	private final OrigineStageRepository origineStageRepository;

	private Integer idOrigineStage;

	@Autowired
	OrigineStageRepositoryTest(final EntityManager entityManager, final OrigineStageRepository origineStageRepository) {
		super();
		this.entityManager = entityManager;
		this.origineStageRepository = origineStageRepository;
	}

	@BeforeEach
	void prepare() {
		final OrigineStage origineStage = new OrigineStage();
		origineStage.setLibelleOrigineStage("Label");
		origineStage.setTemEnServOrigineStage("A");
		origineStage.setModifiable(true);

		entityManager.persist(origineStage);
		entityManager.flush();

		entityManager.refresh(origineStage);
		this.idOrigineStage = origineStage.getIdOrigineStage();
	}

	@Test
	@DisplayName("findById – Nominal test case")
	void findById() {
		final Optional<OrigineStage> result = origineStageRepository.findById(this.idOrigineStage);
		assertTrue(result.isPresent(), "We should have found our entity");

		final OrigineStage tmp = result.get();
		assertEquals("Label", tmp.getLibelleOrigineStage());
		assertEquals("A", tmp.getTemEnServOrigineStage());
		assertTrue(tmp.isModifiable());
	}

}
