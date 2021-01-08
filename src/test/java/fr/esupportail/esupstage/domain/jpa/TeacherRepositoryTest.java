package fr.esupportail.esupstage.domain.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import fr.esupportail.esupstage.AbstractTest;

@Rollback
@Transactional
class TeacherRepositoryTest extends AbstractTest {

	private final EntityManager entityManager;

	private final TeacherRepository repository;

	@Autowired
	TeacherRepositoryTest(final EntityManager entityManager, final TeacherRepository teacherRepository) {
		super();
		this.entityManager = entityManager;
		this.repository = teacherRepository;
	}

	@BeforeEach
	void prepare() {
		final Teacher entity = new Teacher();
		entity.setBirthDate(LocalDate.of(1980, 01, 01));
		entity.setEmail("jdoe@uphf.fr");
		entity.setFirstName("John");
		entity.setLastName("Doe");

		entityManager.persist(entity);
		entityManager.flush();
	}

	@Test
	@DisplayName("findById – Nominal test case")
	void findById() {
		final Optional<Teacher> result = repository.findById("jdoe@uphf.fr");
		assertTrue(result.isPresent(), "We should have found our entity");

		final Teacher tmp = result.get();
		assertEquals("jdoe@uphf.fr", tmp.getEmail());
		assertEquals("John", tmp.getFirstName());
		assertEquals("Doe", tmp.getLastName());
		assertEquals(LocalDate.of(1980, 01, 01), tmp.getBirthDate());
	}

}
