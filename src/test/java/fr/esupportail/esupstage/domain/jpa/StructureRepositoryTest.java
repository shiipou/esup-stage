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
import fr.esupportail.esupstage.domain.jpa.entities.Effectif;
import fr.esupportail.esupstage.domain.jpa.entities.Pays;
import fr.esupportail.esupstage.domain.jpa.entities.Structure;
import fr.esupportail.esupstage.domain.jpa.entities.TypeStructure;
import fr.esupportail.esupstage.domain.jpa.repositories.StructureRepository;

@Rollback
@Transactional
public class StructureRepositoryTest extends AbstractTest {

	private final EntityManager entityManager;

	private final StructureRepository repository;

	private Integer id;

	@Autowired
	StructureRepositoryTest(final EntityManager entityManager, final StructureRepository repository) {
		super();
		this.entityManager = entityManager;
		this.repository = repository;
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

		final Effectif effectif = new Effectif();
		effectif.setLibelleEffectif("Label");
		effectif.setTemEnServEffectif("A");
		entityManager.persist(effectif);

		final TypeStructure typeStructure = new TypeStructure();
		typeStructure.setTemEnServTypeStructure("A");
		typeStructure.setSiretObligatoire(true);
		typeStructure.setLibelleTypeStructure("Label");
		entityManager.persist(typeStructure);

		final Structure structure = new Structure();
		structure.setEstValidee(1);
		structure.setRaisonSociale("ESN");
		structure.setVoie("Street");
		structure.setPay(pays);
		structure.setEffectif(effectif);
		structure.setTypeStructure(typeStructure);
		entityManager.persist(structure);

		entityManager.flush();
		entityManager.refresh(structure);
		id = structure.getIdStructure();
	}

	@Test
	@DisplayName("findById – Nominal Test Case")
	void findById() {
		final Optional<Structure> result = repository.findById(id);
		assertTrue(result.isPresent(), "We should have found our entity");

		final Structure tmp = result.get();
		assertEquals(1, tmp.getEstValidee());
		assertEquals("Street", tmp.getVoie());
		assertEquals("ESN", tmp.getRaisonSociale());
	}

}
