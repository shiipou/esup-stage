package fr.esupportail.esupstage.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import fr.esupportail.esupstage.AbstractTest;
import fr.esupportail.esupstage.domain.jpa.Teacher;
import fr.esupportail.esupstage.domain.jpa.TeacherRepository;
import fr.esupportail.esupstage.services.beans.TeacherBean;

class TeacherServiceTest extends AbstractTest {

	@Mock
	private TeacherRepository teacherRepository;

	private TeacherService service;

	@BeforeEach
	void prepareForEach() {
		initMocks(this);

		service = new TeacherService(teacherRepository);
	}

	@Test
	@DisplayName("findById – Nominal test case")
	void findById() {
		final LocalDate birthDate = LocalDate.of(1990, 1, 1);

		final Teacher teacher = new Teacher();
		teacher.setBirthDate(birthDate);
		teacher.setEmail("jdoe@esup.org");
		teacher.setFirstName("John");
		teacher.setLastName("Doe");

		given(teacherRepository.findById(eq("jdoe@esup.org"))).willReturn(Optional.of(teacher));

		final TeacherBean result = service.findById("jdoe@esup.org");

		verify(teacherRepository).findById(eq("jdoe@esup.org"));

		assertEquals("John", result.getFirstName());
		assertEquals("Doe", result.getLastName());
		assertEquals("jdoe@esup.org", result.getEmail());
		assertEquals(birthDate, result.getBirthDate());
	}
}
