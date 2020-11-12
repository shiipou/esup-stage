package fr.esupportail.esupstage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.esupportail.esupstage.services.TeacherService;
import fr.esupportail.esupstage.services.beans.TeacherBean;

@RestController
@RequestMapping("/api/enseignants")
public class TeacherController {

	private final TeacherService teacherService;

	@Autowired
	public TeacherController(final TeacherService teacherService) {
		super();
		this.teacherService = teacherService;
	}

	@GetMapping
	public Page<TeacherBean> index(final Pageable pageable) {
		return this.teacherService.findAll(pageable);
	}

	@PostMapping
	public TeacherBean index(@RequestBody final TeacherBean teacher) {
		return this.teacherService.save(teacher);
	}

	@GetMapping("/{email}")
	public TeacherBean findBy(@PathVariable final String email) {
		return this.teacherService.findById(email);
	}

	@DeleteMapping("/{email}")
	public void deleteEnseignant(@PathVariable final String email) {
		this.teacherService.deleteBy(email);
	}
}
