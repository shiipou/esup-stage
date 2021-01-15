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
import fr.esupportail.esupstage.domain.jpa.entities.Affectation;
import fr.esupportail.esupstage.domain.jpa.entities.AffectationPK;
import fr.esupportail.esupstage.domain.jpa.repositories.AffectationRepository;

@Rollback
@Transactional
class AffectationRepositoryTest extends AbstractTest {

	private final EntityManager entityManager;

	private final AffectationRepository affectationRepository;

	private AffectationPK affectationPK;

	@Autowired
	AffectationRepositoryTest(final EntityManager entityManager, final AffectationRepository affectationRepository) {
		super();
		this.entityManager = entityManager;
		this.affectationRepository = affectationRepository;
	}

	@BeforeEach
	void prepare() {

		final AffectationPK affectationPK = new AffectationPK();
		affectationPK.setCodeAffectation("code");
		affectationPK.setCodeUniversite("univ");

		final Affectation affectation = new Affectation();
		affectation.setLibelleAffectation("libel");
		affectation.setId(affectationPK);

		entityManager.persist(affectation);
		this.affectationPK = affectation.getId();
		entityManager.flush();
	}

	@Test
	@DisplayName("findById – Nominal test case")
	void findById() {
		final Optional<Affectation> result = affectationRepository.findById(affectationPK);
		assertTrue(result.isPresent(), "We should have found our Affectation");

		final Affectation affectation = result.get();
		assertEquals("code", affectation.getId().getCodeAffectation());
		assertEquals("libel", affectation.getLibelleAffectation());
	}

}
