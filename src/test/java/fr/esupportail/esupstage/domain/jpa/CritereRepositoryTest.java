package fr.esupportail.esupstage.domain.jpa;

import fr.esupportail.esupstage.AbstractTest;
import fr.esupportail.esupstage.domain.jpa.entities.*;
import fr.esupportail.esupstage.domain.jpa.repositories.ConventionRepository;
import fr.esupportail.esupstage.domain.jpa.repositories.CritereRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Rollback
@Transactional
class CritereRepositoryTest extends AbstractTest {

		private final EntityManager entityManager;

		private final CritereRepository critereRepository;
		private int critereId;

		@Autowired CritereRepositoryTest(final EntityManager entityManager,
				final CritereRepository critereRepository) {
				super();
				this.entityManager = entityManager;
				this.critereRepository = critereRepository;
		}

		@BeforeEach
		void prepare() {

				Niveau niveau = new Niveau();
				niveau.setValeur(1);
				entityManager.persist(niveau);

				Categorie categorie = new Categorie();
				categorie.setTypeCategorie(1);
				entityManager.persist(categorie);

				final Critere critere = new Critere();
				critere.setClef("key");
				critere.setValeur("value");
				critere.setCategorie(categorie);
				critere.setNiveauBean(niveau);
				entityManager.persist(critere);

				this.critereId = critere.getIdCritere();
				entityManager.flush();
		}


		@Test
		@DisplayName("findById – Nominal test case")
		void findById() {
				final Optional<Critere> result = critereRepository.findById(this.critereId);
				assertTrue(result.isPresent(), "We should have found our Critere");

				final Critere critere = result.get();
				assertEquals("key",critere.getClef());
				assertEquals("value",critere.getValeur());
				assertEquals(1,critere.getCategorie().getTypeCategorie());
				assertEquals(1,critere.getNiveauBean().getValeur());
		}


}