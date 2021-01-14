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
class CategorieRepositoryTest extends AbstractTest {

		private final EntityManager entityManager;

		private final CategorieRepository categorieRepository;
		private int categorieId;

		@Autowired
		CategorieRepositoryTest(final EntityManager entityManager,
				final CategorieRepository categorieRepository) {
				super();
				this.entityManager = entityManager;
				this.categorieRepository = categorieRepository;
		}

		@BeforeEach
		void prepare() {

				Categorie categorie = new Categorie();
				categorie.setTypeCategorie(1);

				entityManager.persist(categorie);
				this.categorieId = categorie.getIdCategorie();
				entityManager.flush();
		}


		@Test
		@DisplayName("findById – Nominal test case")
		void findById() {
				final Optional<Categorie> result = categorieRepository.findById(this.categorieId);
				assertTrue(result.isPresent(), "We should have found our Categorie");

				final Categorie categorie = result.get();
				assertEquals(1,categorie.getTypeCategorie());

		}

}
