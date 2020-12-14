package fr.esupportail.esupstage.domain.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.esupportail.esupstage.domain.jpa.entities.OffreDiffusion;
import fr.esupportail.esupstage.domain.jpa.entities.OffreDiffusionPK;

@Repository
public interface OffreDiffusionRepository extends JpaRepository<OffreDiffusion, OffreDiffusionPK> {
}
