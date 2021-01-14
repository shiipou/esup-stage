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
import fr.esupportail.esupstage.domain.jpa.entities.TicketStructure;
import fr.esupportail.esupstage.domain.jpa.repositories.TicketStructureRepository;

@Rollback
@Transactional
public class TicketStructureRepositoryTest extends AbstractTest {

	private final EntityManager entityManager;

	private final TicketStructureRepository repository;

	@Autowired
	TicketStructureRepositoryTest(final EntityManager entityManager, final TicketStructureRepository repository) {
		super();
		this.entityManager = entityManager;
		this.repository = repository;
	}

	@BeforeEach
	void prepare() {
		final TicketStructure ticketStructure = new TicketStructure();
		ticketStructure.setTicket("Ticket");
		ticketStructure.setIdStructure(1);
		entityManager.persist(ticketStructure);
		entityManager.flush();
	}

	@Test
	@DisplayName("findById – Nominal Test Case")
	void findById() {
		final Optional<TicketStructure> result = repository.findById("Ticket");
		assertTrue(result.isPresent(), "We should have found our entity");

		final TicketStructure tmp = result.get();
		assertEquals("Ticket", tmp.getTicket());
		assertEquals(1, tmp.getIdStructure());
	}

}
