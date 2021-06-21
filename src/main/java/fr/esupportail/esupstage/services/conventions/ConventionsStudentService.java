package fr.esupportail.esupstage.services.conventions;

import java.net.Authenticator;
import java.net.URI;
import java.net.http.HttpClient;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.Person;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

import fr.esupportail.esupstage.domain.jpa.entities.Etudiant;
import fr.esupportail.esupstage.domain.jpa.repositories.EtudiantRepository;
import fr.esupportail.esupstage.services.beans.conventions.ConventionBean;
import javassist.NotFoundException;
import reactor.core.publisher.Mono;

@Named
@ApplicationScoped
public class ConventionsStudentService {

    @Value("${application.restService.base-url}")
    private String apiUrl;
    @Value("${application.restService.user}")
    private String apiUser;
    @Value("${application.restService.pass}")
    private String apiPass;

    @Autowired
    private EtudiantRepository studentRepository;

    // public List<ConventionBean> getConventions() {
    // return this.conventionRepository.findAll();
    // }

    public Etudiant getStudentByIdAndCodeUniv(int id, String codeUniv) throws NoSuchElementException {
        return this.studentRepository.findEtudiantByIdAndCodeUniversite(id, codeUniv).orElseThrow();
    }

    public Etudiant getStudentByIdentAndCodeUniv(String username, String codeUniv) throws NoSuchElementException {
        return this.studentRepository.findEtudiantByIdentEtudiantAndCodeUniversite(username, codeUniv).orElseThrow();
    }

    public ConventionBean updateConvention(ConventionBean convention) throws NotFoundException {
        // TODO: save ConventionBean into convention
        return convention;
    }

    public Etudiant updateStudent(fr.esupportail.esupstage.controllers.jsf.beans.conventions.StudentBean student) throws NotFoundException {
        Etudiant etudiant = studentRepository
                .findEtudiantByIdAndCodeUniversite(student.getId(), student.getCodeUniversite())
                .orElseThrow(() -> new NotFoundException("Student not found"));
        this.studentRepository.save(etudiant);
        return etudiant;
    }
}
