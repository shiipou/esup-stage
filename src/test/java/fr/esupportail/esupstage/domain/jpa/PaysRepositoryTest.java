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
import fr.esupportail.esupstage.domain.jpa.entities.Pays;
import fr.esupportail.esupstage.domain.jpa.repositories.PaysRepository;

@Rollback
@Transactional
public class PaysRepositoryTest extends AbstractTest {

	private final EntityManager entityManager;

	private final PaysRepository paysRepository;

	private Integer idPays;

	@Autowired
	PaysRepositoryTest(final EntityManager entityManager, final PaysRepository paysRepository) {
		super();
		this.entityManager = entityManager;
		this.paysRepository = paysRepository;
	}

	@BeforeEach
	void prepare() {
		final Pays pays = new Pays();
		pays.setActual(1);
		pays.setCog(1);
		pays.setLib("Label");
		pays.setSiretObligatoire(true);
		pays.setTemEnServPays("A");

		entityManager.persist(pays);
		entityManager.flush();

		entityManager.refresh(pays);
		this.idPays = pays.getIdPays();
	}

	@Test
	@DisplayName("findById – Nominal test case")
	void findById() {
		final Optional<Pays> result = this.paysRepository.findById(idPays);
		assertTrue(result.isPresent(), "We should have found our entity");

		final Pays tmp = result.get();
		assertEquals("Label", tmp.getLib());
		assertEquals(1, tmp.getActual());
		assertEquals(1, tmp.getCog());
		assertEquals("A", tmp.getTemEnServPays());
		assertTrue(tmp.isSiretObligatoire());
	}

}
