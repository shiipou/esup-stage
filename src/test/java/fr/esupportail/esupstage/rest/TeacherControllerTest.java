package fr.esupportail.esupstage.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.esupportail.esupstage.AbstractTest;
import fr.esupportail.esupstage.exception.NotFoundException;
import fr.esupportail.esupstage.services.TeacherService;
import fr.esupportail.esupstage.services.beans.TeacherBean;

@AutoConfigureMockMvc
public class TeacherControllerTest extends AbstractTest {

	private final MockMvc mockMvc;

	private final ObjectMapper objectMapper;

	@MockBean
	private TeacherService teacherService;

	private JacksonTester<TeacherBean> jsonTeacher;

	@Autowired
	TeacherControllerTest(final MockMvc mockMvc, final ObjectMapper objectMapper) {
		this.mockMvc = mockMvc;
		this.objectMapper = objectMapper;
	}

	@BeforeEach
	void prepare() {
		JacksonTester.initFields(this, objectMapper);
	}

	@Test
	@DisplayName("findAll – Nominal test case")
	@WithMockUser(username = "jdoe", password = "jdoe", roles = { "USER" })
	void findAll01() throws Exception {
		final TeacherBean teacher = new TeacherBean();
		teacher.setBirthDate(LocalDate.of(1990, 1, 20));
		teacher.setEmail("jdoe@esup.org");
		teacher.setFirstName("John");
		teacher.setLastName("Doe");

		when(teacherService.findAll(any())).thenReturn(new PageImpl<>(Arrays.asList(teacher)));

		// @formatter:off
		mockMvc.perform(get("/api/teachers"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.content").isArray())
			.andExpect(jsonPath("$.content[0]").exists())
			.andExpect(jsonPath("$.content[0].lastName").value("Doe"))
			.andExpect(jsonPath("$.content[0].firstName").value("John"))
			.andExpect(jsonPath("$.content[0].email").value("jdoe@esup.org"))
			.andExpect(jsonPath("$.content[0].birthDate").value("1990-01-20"))
		;
		// @formatter:on

		verify(teacherService).findAll(any());
	}

	@Test
	@DisplayName("findBy – Nominal test case")
	@WithMockUser(username = "jdoe", password = "jdoe", roles = { "USER" })
	void findBy01() throws Exception {
		final TeacherBean teacher = new TeacherBean();
		teacher.setBirthDate(LocalDate.of(1990, 1, 20));
		teacher.setEmail("jdoe@esup.org");
		teacher.setFirstName("John");
		teacher.setLastName("Doe");
		when(teacherService.findById(eq("jdoe@esup.org"))).thenReturn(teacher);

		// @formatter:off
		mockMvc.perform(get("/api/teachers/{email}", "jdoe@esup.org"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.lastName").value("Doe"))
			.andExpect(jsonPath("$.firstName").value("John"))
			.andExpect(jsonPath("$.email").value("jdoe@esup.org"))
			.andExpect(jsonPath("$.birthDate").value("1990-01-20"))
		;
		// @formatter:on

		verify(teacherService).findById(eq("jdoe@esup.org"));
	}

	@Test
	@DisplayName("findBy – Not Found")
	@WithMockUser(username = "jdoe", password = "jdoe", roles = { "USER" })
	void findBy02() throws Exception {
		when(teacherService.findById(eq("jdoe@esup.org"))).thenThrow(new NotFoundException());

		// @formatter:off
		mockMvc.perform(get("/api/teachers/{email}", "jdoe@esup.org"))
			.andExpect(status().isNotFound())
		;
		// @formatter:on

		verify(teacherService).findById(eq("jdoe@esup.org"));
	}

	@Test
	@DisplayName("add – Nominal test case")
	@WithMockUser(username = "jdoe", password = "jdoe", roles = { "USER" })
	void add01() throws Exception {
		final TeacherBean teacher = new TeacherBean();
		teacher.setBirthDate(LocalDate.of(1990, 1, 20));
		teacher.setEmail("jdoe@esup.org");
		teacher.setFirstName("John");
		teacher.setLastName("Doe");

		when(teacherService.save(any())).thenAnswer(param -> param.getArgument(0));

		// @formatter:off
		final MockHttpServletRequestBuilder builder = post("/api/teachers")
			.accept(MediaType.APPLICATION_JSON)
			.content(jsonTeacher.write(teacher).getJson())
			.contentType(MediaType.APPLICATION_JSON)
			.with(csrf())
		;

		mockMvc.perform(builder)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.lastName").value("Doe"))
			.andExpect(jsonPath("$.firstName").value("John"))
			.andExpect(jsonPath("$.email").value("jdoe@esup.org"))
			.andExpect(jsonPath("$.birthDate").value("1990-01-20"))
		;
		// @formatter:on

		final ArgumentCaptor<TeacherBean> captor = ArgumentCaptor.forClass(TeacherBean.class);
		verify(teacherService).save(captor.capture());

		final TeacherBean savedTeacher = captor.getValue();
		assertEquals("jdoe@esup.org", savedTeacher.getEmail());
		assertEquals("John", savedTeacher.getFirstName());
		assertEquals("Doe", savedTeacher.getLastName());
		assertEquals(LocalDate.of(1990, 1, 20), savedTeacher.getBirthDate());
	}

	@Test
	@DisplayName("delete – Nominal test case")
	@WithMockUser(username = "jdoe", password = "jdoe", roles = { "USER" })
	void delete01() throws Exception {

		// @formatter:off
		final MockHttpServletRequestBuilder builder = delete("/api/teachers/{email}", "jdoe@esup.org")
			.with(csrf())
		;

		mockMvc.perform(builder)
			.andExpect(status().isOk())
		;
		// @formatter:on

		verify(teacherService).deleteById(eq("jdoe@esup.org"));
	}

}
