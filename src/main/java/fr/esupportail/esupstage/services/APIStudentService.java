package fr.esupportail.esupstage.services;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

import fr.esupportail.esupstage.domain.jpa.entities.Etudiant;
import fr.esupportail.esupstage.domain.jpa.repositories.EtudiantRepository;
import fr.esupportail.esupstage.services.beans.conventions.APIStudentBean;
import reactor.core.publisher.Mono;

@Named
@ApplicationScoped
public class APIStudentService {

    @Value("${application.restService.base-url}")
    private static String apiUrl;
    @Value("${application.restService.user}")
    private static String apiUser;
    @Value("${application.restService.pass}")
    private static String apiPass;

    @Value("${application.restService.ldapUsernameField}")
    private static String ldapUsernameField;

    @Autowired
    private EtudiantRepository studentRepository;

    // public List<ConventionBean> getConventions() {
    // return this.conventionRepository.findAll();
    // }

    public APIStudentBean getStudent(String id, String codeUniv) {
        ResponseSpec responseSpec = WebClient.builder().baseUrl(apiUrl)
				.defaultHeaders(header -> header.setBasicAuth(apiUser, apiPass))
				.build()
				.get()
				.uri(uriBuilder -> uriBuilder.path("https://referentiel-test.parisnanterre.fr/ldap/byFilter")
				.queryParam("filter", new StringBuilder().append("(").append(ldapUsernameField).append("=").append(id.replaceAll("*", "")).append(")").toString())
				.build())
				.retrieve();

		Mono<APIStudentBean> mono = responseSpec.bodyToMono(APIStudentBean.class);
		APIStudentBean apogeeMap = mono.block();
        return apogeeMap;
    }

    public Etudiant getStudentFromUser(UserDetails detail, String codeUniversite) {
        APIStudentBean apiStudent = this.getStudent(detail.getUsername(), codeUniversite);
        // Etudiant student = apiStudent;
        return null;
    }
}
