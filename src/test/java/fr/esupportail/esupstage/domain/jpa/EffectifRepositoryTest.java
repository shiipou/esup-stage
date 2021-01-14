package fr.esupportail.esupstage.domain.jpa;

import fr.esupportail.esupstage.AbstractTest;
import fr.esupportail.esupstage.domain.jpa.entities.DureeDiffusion;
import fr.esupportail.esupstage.domain.jpa.entities.Effectif;
import fr.esupportail.esupstage.domain.jpa.repositories.DureeDiffusionRepository;
import fr.esupportail.esupstage.domain.jpa.repositories.EffectifRepository;
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
class EffectifRepositoryTest extends AbstractTest {

		private final EntityManager entityManager;

		private final EffectifRepository effectifRepository;
		private int effectId;

		@Autowired EffectifRepositoryTest(final EntityManager entityManager, final EffectifRepository effectifRepository) {
				super();
				this.entityManager = entityManager;
				this.effectifRepository = effectifRepository;
		}

		@BeforeEach void prepare() {

				Effectif effectif = new Effectif();
				effectif.setLibelleEffectif("libel");
				effectif.setTemEnServEffectif("L");
				entityManager.persist(effectif);

				this.effectId = effectif.getIdEffectif();
				entityManager.flush();
		}

		@Test @DisplayName("findById – Nominal test case") void findById() {
				final Optional<Effectif> result = effectifRepository.findById(this.effectId);
				assertTrue(result.isPresent(), "We should have found our Effectif");

				final Effectif effectif = result.get();
				assertEquals("libel", effectif.getLibelleEffectif());
				assertEquals("L", effectif.getTemEnServEffectif());
		}
}

