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
import org.springframework.scripting.config.LangNamespaceHandler;
import org.springframework.test.annotation.Rollback;

import fr.esupportail.esupstage.AbstractTest;

import static org.junit.jupiter.api.Assertions.*;

@Rollback
@Transactional
class CaisseRegimeRepositoryTest extends AbstractTest {

		private final EntityManager entityManager;

		private final CaisseRegimeRepository caisseRegimeRepository;
		private String caisseRegimeId;

		@Autowired
		CaisseRegimeRepositoryTest(final EntityManager entityManager,
				final CaisseRegimeRepository caisseRegimeRepository) {
				super();
				this.entityManager = entityManager;
				this.caisseRegimeRepository = caisseRegimeRepository;
		}

		@BeforeEach
		void prepare() {

				CaisseRegime caisseRegime = new CaisseRegime();
				caisseRegime.setCodeCaisse("code");
				caisseRegime.setInfoCaisse("info");
				caisseRegime.setLibelleCaisse("libel");
				caisseRegime.setTemEnServCaisse("F");

				entityManager.persist(caisseRegime);
				this.caisseRegimeId = caisseRegime.getCodeCaisse();
				entityManager.flush();
		}


		@Test
		@DisplayName("findById – Nominal test case")
		void findById() {
				final Optional<CaisseRegime> result = caisseRegimeRepository.findById(this.caisseRegimeId);
				assertTrue(result.isPresent(), "We should have found our Assurance");

				final CaisseRegime caisseRegime = result.get();
				assertEquals("code",caisseRegime.getCodeCaisse());
				assertEquals("info",caisseRegime.getInfoCaisse());
				assertEquals("libel",caisseRegime.getLibelleCaisse());
				assertEquals("F",caisseRegime.getTemEnServCaisse());

		}

}
