package fr.esupportail.esupstage.controllers;

import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.esupportail.esupstage.domain.jpa.Enseignant;
import fr.esupportail.esupstage.domain.jpa.EnseignantRepository;

@RestController
@RolesAllowed("USER")
@RequestMapping("/api/enseignants")
public class EnseignantController {

	@Autowired
	private EnseignantRepository enseignantRepository;

	/**
	 * @param auth
	 * @return
	 */
	@GetMapping
	public List<Enseignant> index() {
		return enseignantRepository.findAll();
	}

	@PostMapping
	public Enseignant index(@RequestBody Enseignant enseignant) {
		enseignantRepository.save(enseignant);
		return enseignant;
	}

	@DeleteMapping("/{id}")
	public void deleteEnseignant(@PathVariable Long id) {
		enseignantRepository.deleteById(id);
	}
}
