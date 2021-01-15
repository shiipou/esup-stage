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
import fr.esupportail.esupstage.domain.jpa.entities.Assurance;
import fr.esupportail.esupstage.domain.jpa.repositories.AssuranceRepository;

@Rollback
@Transactional
class AssuranceRepositoryTest extends AbstractTest {

	private final EntityManager entityManager;

	private final AssuranceRepository assuranceRepository;

	private int assuranceId;

	@Autowired
	AssuranceRepositoryTest(final EntityManager entityManager, final AssuranceRepository assuranceRepository) {
		super();
		this.entityManager = entityManager;
		this.assuranceRepository = assuranceRepository;
	}

	@BeforeEach
	void prepare() {

		final Assurance assurance = new Assurance();
		assurance.setLibelleAssurance("libel");
		assurance.setCodeCtrl("code");
		assurance.setTemEnServAss("F");

		entityManager.persist(assurance);
		assuranceId = assurance.getIdAssurance();
		entityManager.flush();
	}

	@Test
	@DisplayName("findById – Nominal test case")
	void findById() {
		final Optional<Assurance> result = assuranceRepository.findById(assuranceId);
		assertTrue(result.isPresent(), "We should have found our Assurance");

		final Assurance assurance = result.get();
		assertEquals("libel", assurance.getLibelleAssurance());
		assertEquals("code", assurance.getCodeCtrl());
		assertEquals("F", assurance.getTemEnServAss());
	}

}
