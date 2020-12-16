package fr.esupportail.esupstage.services;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import fr.esupportail.esupstage.AbstractTest;
import fr.esupportail.esupstage.domain.jpa.Teacher;
import fr.esupportail.esupstage.domain.jpa.TeacherRepository;
import fr.esupportail.esupstage.exception.NotFoundException;
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
	@DisplayName("findAll – Nominal test case")
	void findAll01() {
		final LocalDate birthDate = LocalDate.of(1990, 1, 1);

		final Teacher teacher = new Teacher();
		teacher.setBirthDate(birthDate);
		teacher.setEmail("jdoe@esup.org");
		teacher.setFirstName("John");
		teacher.setLastName("Doe");

		when(teacherRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(Arrays.asList(teacher)));

		final Page<TeacherBean> result = service.findAll(Pageable.unpaged());

		verify(teacherRepository).findAll(any(Pageable.class));

		final Iterator<TeacherBean> iterator = result.iterator();
		assertTrue(iterator.hasNext());

		final TeacherBean tmpTeacher = iterator.next();
		assertEquals("John", tmpTeacher.getFirstName());
		assertEquals("Doe", tmpTeacher.getLastName());
		assertEquals("jdoe@esup.org", tmpTeacher.getEmail());
		assertEquals(birthDate, tmpTeacher.getBirthDate());

		assertFalse(iterator.hasNext());
	}

	@Test
	@DisplayName("findById – Nominal test case")
	void findById01() {
		final LocalDate birthDate = LocalDate.of(1990, 1, 1);

		final Teacher teacher = new Teacher();
		teacher.setBirthDate(birthDate);
		teacher.setEmail("jdoe@esup.org");
		teacher.setFirstName("John");
		teacher.setLastName("Doe");

		when(teacherRepository.findById(eq("jdoe@esup.org"))).thenReturn(Optional.of(teacher));

		final TeacherBean result = service.findById("jdoe@esup.org");

		verify(teacherRepository).findById(eq("jdoe@esup.org"));

		assertEquals("John", result.getFirstName());
		assertEquals("Doe", result.getLastName());
		assertEquals("jdoe@esup.org", result.getEmail());
		assertEquals(birthDate, result.getBirthDate());
	}

	@Test
	@DisplayName("findById – Not Found Exception")
	void findById02() {
		when(teacherRepository.findById(eq("jdoe@esup.org"))).thenThrow(new NotFoundException());

		assertThrows(NotFoundException.class, () -> service.findById("jdoe@esup.org"));

		verify(teacherRepository).findById(eq("jdoe@esup.org"));
	}

	@Test
	@DisplayName("save – Nominal test case")
	void save01() {
		final LocalDate birthDate = LocalDate.of(1990, 1, 1);

		when(teacherRepository.save(any())).thenAnswer(answer -> answer.getArgument(0));

		final TeacherBean toSave = new TeacherBean();
		toSave.setBirthDate(birthDate);
		toSave.setEmail("jdoe@esup.org");
		toSave.setFirstName("John");
		toSave.setLastName("Doe");

		final TeacherBean result = service.save(toSave);

		final ArgumentCaptor<Teacher> captor = ArgumentCaptor.forClass(Teacher.class);
		verify(teacherRepository).save(captor.capture());

		assertEquals("John", result.getFirstName());
		assertEquals("Doe", result.getLastName());
		assertEquals("jdoe@esup.org", result.getEmail());
		assertEquals(birthDate, result.getBirthDate());

		final Teacher saved = captor.getValue();
		assertEquals("John", saved.getFirstName());
		assertEquals("Doe", saved.getLastName());
		assertEquals("jdoe@esup.org", saved.getEmail());
		assertEquals(birthDate, saved.getBirthDate());
	}

	@Test
	@DisplayName("deleteBy – Nominal Test Case")
	void deleteBy01() {
		service.deleteById("jdoe@esup.org");

		verify(teacherRepository).deleteById(eq("jdoe@esup.org"));
	}

}
