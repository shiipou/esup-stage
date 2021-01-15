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
import fr.esupportail.esupstage.domain.jpa.entities.Ufr;
import fr.esupportail.esupstage.domain.jpa.entities.UfrPK;
import fr.esupportail.esupstage.domain.jpa.repositories.UfrRepository;

@Rollback
@Transactional
public class UfrRepositoryTest extends AbstractTest {

	private final EntityManager entityManager;

	private final UfrRepository repository;

	@Autowired
	UfrRepositoryTest(final EntityManager entityManager, final UfrRepository repository) {
		super();
		this.entityManager = entityManager;
		this.repository = repository;
	}

	@BeforeEach
	void prepare() {
		final Ufr ufr = new Ufr();
		ufr.setId(new UfrPK("Code", "ESUP"));
		ufr.setLibelleUFR("Label");
		entityManager.persist(ufr);

		entityManager.flush();
		entityManager.refresh(ufr);
	}

	@Test
	@DisplayName("findById – Nominal Test Case")
	void findById() {
		final Optional<Ufr> result = repository.findById(new UfrPK("Code", "ESUP"));
		assertTrue(result.isPresent(), "We should have found our entity");

		final Ufr tmp = result.get();
		assertEquals("Label", tmp.getLibelleUFR());
	}

}
