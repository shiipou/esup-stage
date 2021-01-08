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
import fr.esupportail.esupstage.domain.jpa.entities.PstagedataUser;
import fr.esupportail.esupstage.domain.jpa.repositories.PstagedataUserRepository;

@Rollback
@Transactional
public class PstagedataUserRepositoryTest extends AbstractTest {

	private final EntityManager entityManager;

	private final PstagedataUserRepository repository;

	@Autowired
	PstagedataUserRepositoryTest(final EntityManager entityManager, final PstagedataUserRepository repository) {
		super();
		this.entityManager = entityManager;
		this.repository = repository;
	}

	@BeforeEach
	void prepare() {
		final PstagedataUser pstagedataUser = new PstagedataUser();
		pstagedataUser.setId("jdoe");
		pstagedataUser.setAdmi(true);
		pstagedataUser.setLang("FR");
		entityManager.persist(pstagedataUser);
	}

	@Test
	@DisplayName("findById – Nominal Test Case")
	void findById() {
		final Optional<PstagedataUser> result = repository.findById("jdoe");
		assertTrue(result.isPresent(), "We should have found our entity");

		final PstagedataUser tmp = result.get();
		assertTrue(tmp.isAdmi());
		assertEquals("FR", tmp.getLang());
	}

}
