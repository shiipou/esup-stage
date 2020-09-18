package fr.esupportail.esupstage.controllers;

import fr.esupportail.esupstage.UserInfos;
import fr.esupportail.esupstage.domain.dto.Account;
import fr.esupportail.esupstage.domain.ldap.LdapUserRepository;
import fr.esupportail.esupstage.model.Enseignant;
import fr.esupportail.esupstage.repositories.EnseignantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
public class EnseignantController {

    @Value("${server.frontend.url}")
    private String urlFrontend;

    @Autowired
    private EnseignantRepository enseignantRepository;

    /**
     *
     * @param auth
     * @return
     */
    @GetMapping("/api/enseignant")
    public List<Enseignant> index(@ApiIgnore Authentication auth) {
        List<Enseignant> teachers = enseignantRepository.findAll();
        return enseignantRepository.findAll();
    }

    @PostMapping("/api/enseignant")
    public Enseignant index(@ApiIgnore Authentication auth, @RequestBody @Valid Enseignant enseignant) {
        enseignantRepository.save(enseignant);
        return enseignant;
    }

    @DeleteMapping("/api/enseignant/{id}")
    public void deleteEnseignant(@ApiIgnore Authentication auth, @PathVariable Long id) {
        enseignantRepository.deleteById(id);
    }
}
