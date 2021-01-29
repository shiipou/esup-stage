package fr.esupportail.esupstage.domain.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import fr.esupportail.esupstage.AbstractTest;
import fr.esupportail.esupstage.domain.jpa.entities.FapN1;
import fr.esupportail.esupstage.domain.jpa.entities.FapN2;
import fr.esupportail.esupstage.domain.jpa.entities.FapN3;
import fr.esupportail.esupstage.domain.jpa.repositories.FapN3Repository;

@Rollback
@Transactional
class FapN3RepositoryTest extends AbstractTest {

	private final EntityManager entityManager;

	private final FapN3Repository FapN3Repository;

	@Autowired
	FapN3RepositoryTest(final EntityManager entityManager, final FapN3Repository FapN3Repository) {
		super();
		this.entityManager = entityManager;
		this.FapN3Repository = FapN3Repository;
	}

	@BeforeEach
	void prepare() {
		final FapN1 fapN1 = new FapN1();
		fapN1.setCodeFAP_N1("codeFAP_N1");
		fapN1.setLibelle("libelleFAP_N1");
		final FapN2 fapN2 = new FapN2();
		fapN2.setCodeFAP_N2("codeFAP_N2");
		fapN2.setLibelle("libelleFAP_N2");
		fapN1.addFapN2(fapN2);
		final FapN3 fapN3 = new FapN3();
		fapN3.setCodeFAP_N3("codeFAP_N3");
		fapN3.setLibelle("libelleFAP_N3");
		fapN2.addFapN3(fapN3);
		this.entityManager.persist(fapN1);
		this.entityManager.persist(fapN2);
		this.entityManager.persist(fapN3);
		this.entityManager.flush();
	}

	private void testFapFields(int indice, FapN3 fap) {
		switch (indice) {
		case 0:
			assertEquals("codeFAP_N3", fap.getCodeFAP_N3(), "FapN3 code match");
			assertEquals("libelleFAP_N3", fap.getLibelle(), "FapN3 libelle match");
			break;
		}
	}

	@Test
	@DisplayName("findById – Nominal test case")
	void findById() {
		final Optional<FapN3> result = this.FapN3Repository.findById("codeFAP_N3");
		assertTrue(result.isPresent(), "We should have found our FapN3");

		final FapN3 fap = result.get();
		this.testFapFields(0, fap);
	}

	@Test
	@DisplayName("findAll – Nominal test case")
	void findAll() {
		final List<FapN3> result = this.FapN3Repository.findAll();
		assertTrue(result.size() == 1, "We should have found our FapN3");

		final FapN3 fapQualification = result.get(0);
		assertTrue(fapQualification != null, "FapN3 exist");
		this.testFapFields(0, fapQualification);
	}

}
