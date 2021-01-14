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
import fr.esupportail.esupstage.domain.jpa.entities.StatutJuridique;
import fr.esupportail.esupstage.domain.jpa.entities.TypeStructure;
import fr.esupportail.esupstage.domain.jpa.repositories.StatutJuridiqueRepository;

@Rollback
@Transactional
public class StatutJuridiqueRepositoryTest extends AbstractTest {

	private final EntityManager entityManager;

	private final StatutJuridiqueRepository repository;

	private Integer id;

	@Autowired
	StatutJuridiqueRepositoryTest(final EntityManager entityManager, final StatutJuridiqueRepository repository) {
		super();
		this.entityManager = entityManager;
		this.repository = repository;
	}

	@BeforeEach
	void prepare() {
		final TypeStructure typeStructure = new TypeStructure();
		typeStructure.setTemEnServTypeStructure("A");
		typeStructure.setSiretObligatoire(true);
		typeStructure.setLibelleTypeStructure("Label");
		entityManager.persist(typeStructure);

		final StatutJuridique statutJuridique = new StatutJuridique();
		statutJuridique.setLibelleStatutJuridique("Label");
		statutJuridique.setTemEnServStatut("A");
		statutJuridique.setTypeStructure(typeStructure);
		entityManager.persist(statutJuridique);

		entityManager.flush();
		entityManager.refresh(statutJuridique);
		this.id = statutJuridique.getIdStatutJuridique();
	}

	@Test
	@DisplayName("findById – Nominal Test Case")
	void findById() {
		final Optional<StatutJuridique> result = repository.findById(id);
		assertTrue(result.isPresent(), "We should have found our entity");

		final StatutJuridique tmp = result.get();
		assertEquals("A", tmp.getTemEnServStatut());
		assertEquals("Label", tmp.getLibelleStatutJuridique());
	}

}
