package fr.esupportail.esupstage.domain.jpa;

import fr.esupportail.esupstage.AbstractTest;
import fr.esupportail.esupstage.domain.jpa.entities.DroitAdministration;
import fr.esupportail.esupstage.domain.jpa.entities.DureeDiffusion;
import fr.esupportail.esupstage.domain.jpa.repositories.DroitAdministrationRepository;
import fr.esupportail.esupstage.domain.jpa.repositories.DureeDiffusionRepository;
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
class DureeDiffusionRepositoryTest extends AbstractTest {

		private final EntityManager entityManager;

		private final DureeDiffusionRepository dureeDiffusionRepository;
		private int dureeDiffusionId;

		@Autowired DureeDiffusionRepositoryTest(final EntityManager entityManager,
				final DureeDiffusionRepository dureeDiffusionRepository) {
				super();
				this.entityManager = entityManager;
				this.dureeDiffusionRepository = dureeDiffusionRepository;
		}

		@BeforeEach
		void prepare() {

				DureeDiffusion dureeDiffusion = new DureeDiffusion();
				dureeDiffusion.setLibelleDureeDiffusion("libel");
				entityManager.persist(dureeDiffusion);

				this.dureeDiffusionId = dureeDiffusion.getIdDureeDiffusion();
				entityManager.flush();
		}


		@Test
		@DisplayName("findById – Nominal test case")
		void findById() {
				final Optional<DureeDiffusion> result = dureeDiffusionRepository.findById(this.dureeDiffusionId);
				assertTrue(result.isPresent(), "We should have found our DureeDiffusion");

				final DureeDiffusion dureeDiffusion = result.get();
				assertEquals("libel",dureeDiffusion.getLibelleDureeDiffusion());
		}


}