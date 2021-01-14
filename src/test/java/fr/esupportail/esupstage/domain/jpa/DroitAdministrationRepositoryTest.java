package fr.esupportail.esupstage.domain.jpa;

import fr.esupportail.esupstage.AbstractTest;
import fr.esupportail.esupstage.domain.jpa.entities.Categorie;
import fr.esupportail.esupstage.domain.jpa.entities.Critere;
import fr.esupportail.esupstage.domain.jpa.entities.DroitAdministration;
import fr.esupportail.esupstage.domain.jpa.entities.Niveau;
import fr.esupportail.esupstage.domain.jpa.repositories.CritereRepository;
import fr.esupportail.esupstage.domain.jpa.repositories.DroitAdministrationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Rollback
@Transactional
class DroitAdministrationRepositoryTest extends AbstractTest {

		private final EntityManager entityManager;

		private final DroitAdministrationRepository droitAdministrationRepository;
		private int droitAdministrationId;

		@Autowired DroitAdministrationRepositoryTest(final EntityManager entityManager,
				final DroitAdministrationRepository droitAdministrationRepository) {
				super();
				this.entityManager = entityManager;
				this.droitAdministrationRepository = droitAdministrationRepository;
		}

		@BeforeEach
		void prepare() {

				DroitAdministration droitAdministration = new DroitAdministration();
				droitAdministration.setLibelleDroitAdministration("libel");
				droitAdministration.setTemEnServDroitAdmin("F");
				entityManager.persist(droitAdministration);

				this.droitAdministrationId = droitAdministration.getIdDroitAdministration();
				entityManager.flush();
		}


		@Test
		@DisplayName("findById – Nominal test case")
		void findById() {
				final Optional<DroitAdministration> result = droitAdministrationRepository.findById(this.droitAdministrationId);
				assertTrue(result.isPresent(), "We should have found our DroitAdministration");

				final DroitAdministration droitAdministration = result.get();
				assertEquals("libel",droitAdministration.getLibelleDroitAdministration());
				assertEquals("F",droitAdministration.getTemEnServDroitAdmin());
		}


}