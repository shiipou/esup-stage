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
class ConfidentialiteRepositoryTest extends AbstractTest {

		private final EntityManager entityManager;

		private final ConfidentialiteRepository confidentialiteRepository;
		private String confidentialiteId;

		@Autowired ConfidentialiteRepositoryTest (final EntityManager entityManager,
				final ConfidentialiteRepository confidentialiteRepository) {
				super();
				this.entityManager = entityManager;
				this.confidentialiteRepository = confidentialiteRepository;
		}

		@BeforeEach
		void prepare() {
				final Confidentialite confidentialite = new Confidentialite();
				confidentialite.setCodeConfidentialite("C");
				confidentialite.setLibelleConfidentialite("libel");
				confidentialite.setTemEnServConfid("C");

				entityManager.persist(confidentialite);

				this.confidentialiteId = confidentialite.getCodeConfidentialite();
				entityManager.flush();
		}


		@Test
		@DisplayName("findById – Nominal test case")
		void findById() {
				final Optional<Confidentialite> result = confidentialiteRepository.findById(this.confidentialiteId);
				assertTrue(result.isPresent(), "We should have found our Confidentialite");

				final Confidentialite confidentialite = result.get();
				assertEquals("C",confidentialite.getCodeConfidentialite());
				assertEquals("libel",confidentialite.getLibelleConfidentialite());
				assertEquals("C",confidentialite.getTemEnServConfid());
		}

}
