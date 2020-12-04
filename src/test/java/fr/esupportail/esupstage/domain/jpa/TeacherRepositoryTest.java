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
public class TeacherRepositoryTest extends AbstractTest {

	private final EntityManager entityManager;

	private final TeacherRepository teacherRepository;

	@Autowired
	public TeacherRepositoryTest(final EntityManager entityManager, final TeacherRepository teacherRepository) {
		super();
		this.entityManager = entityManager;
		this.teacherRepository = teacherRepository;
	}

	@BeforeEach
	public void prepare() {
		final Teacher teacher = new Teacher();
		teacher.setBirthDate(LocalDate.of(1980, 01, 01));
		teacher.setEmail("jdoe@uphf.fr");
		teacher.setFirstName("John");
		teacher.setLastName("Doe");

		this.entityManager.persist(teacher);
		this.entityManager.flush();
	}

	@Test
	@DisplayName("findById – Nominal test case")
	public void findById() {
		final Optional<Teacher> result = this.teacherRepository.findById("jdoe@uphf.fr");
		assertTrue(result.isPresent(), "We should have found our teacher");

		final Teacher teacher = result.get();
		assertEquals("jdoe@uphf.fr", teacher.getEmail());
		assertEquals("John", teacher.getFirstName());
		assertEquals("Doe", teacher.getLastName());
		assertEquals(LocalDate.of(1980, 01, 01), teacher.getBirthDate());
	}

}
