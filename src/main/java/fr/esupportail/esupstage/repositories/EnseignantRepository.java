package fr.esupportail.esupstage.repositories;

import fr.esupportail.esupstage.model.Enseignant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnseignantRepository extends JpaRepository<Enseignant, Long> {
}
