package fr.esupportail.esupstage.domain.jpa;

import java.util.Date;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import fr.esupportail.esupstage.domain.jpa.entities.*;
import fr.esupportail.esupstage.domain.jpa.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import fr.esupportail.esupstage.AbstractTest;

import static org.junit.jupiter.api.Assertions.*;

@Rollback
@Transactional
class CiviliteRepositoryTest extends AbstractTest {

		private final EntityManager entityManager;

		private final CiviliteRepository civiliteRepository;
		private int civiliteId;

		@Autowired CiviliteRepositoryTest(final EntityManager entityManager,
				final CiviliteRepository civiliteRepository) {
				super();
				this.entityManager = entityManager;
				this.civiliteRepository = civiliteRepository;
		}

		@BeforeEach
		void prepare() {
				final AdminStructure adminStructure = new AdminStructure();
				adminStructure.setDateCreation(new Date(0));
				adminStructure.setLoginCreation("login");

				entityManager.persist(adminStructure);

				final Civilite civilite = new Civilite();
				civilite.setLibelleCivilite("libel");
				civilite.addAdminStructure(adminStructure);
				entityManager.persist(civilite);

				this.civiliteId= civilite.getIdCivilite();
				entityManager.flush();
		}


		@Test
		@DisplayName("findById – Nominal test case")
		void findById() {
				final Optional<Civilite> result = civiliteRepository.findById(this.civiliteId);
				assertTrue(result.isPresent(), "We should have found our Civilite");

				final Civilite civilite = result.get();
				assertEquals("login",civilite.getAdminStructures().get(0).getLoginCreation());
				assertEquals("libel",civilite.getLibelleCivilite());
		}

}
