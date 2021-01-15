package fr.esupportail.esupstage.domain.jpa;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.test.annotation.Rollback;

import fr.esupportail.esupstage.AbstractTest;
import fr.esupportail.esupstage.domain.jpa.entities.AdminStructure;
import fr.esupportail.esupstage.domain.jpa.repositories.AdminStructureRepository;

@Rollback
@Transactional
class AdminStructureRepositoryTest extends AbstractTest {

	private final EntityManager entityManager;

	private final AdminStructureRepository adminStructureRepository;

	@Autowired
	AdminStructureRepositoryTest(final EntityManager entityManager, final AdminStructureRepository adminStructureRepository) {
		super();
		this.entityManager = entityManager;
		this.adminStructureRepository = adminStructureRepository;
	}

	@BeforeEach
	void prepare() {

		final AdminStructure adminStructure = new AdminStructure();
		adminStructure.setDateCreation(new Date(0));
		adminStructure.setLoginCreation("login");

		entityManager.persist(adminStructure);
		entityManager.flush();

	}

	@Test
	@DisplayName("findById – Nominal test case")
	void findById() {
		final AdminStructure adminStructure = new AdminStructure();
		adminStructure.setDateCreation(new Date(0));
		adminStructure.setLoginCreation("login");

		final Optional<AdminStructure> result = adminStructureRepository.findOne(Example.of(adminStructure));
		assertTrue(result.isPresent(), "We should have found our teacher");

		final AdminStructure adminStructure1 = result.get();

	}

}
