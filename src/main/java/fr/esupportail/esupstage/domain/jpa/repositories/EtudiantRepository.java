package fr.esupportail.esupstage.domain.jpa.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.esupportail.esupstage.domain.jpa.entities.Etudiant;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Integer> {

	public List<Etudiant> findEtudiantsByNomContainsOrPrenomContains(String nom, String prenom);

	public Optional<Etudiant> findEtudiantByIdAndCodeUniversite(Integer idEtudiant, String codeUniveriste);

	public Optional<Etudiant> findEtudiantByIdentEtudiantAndCodeUniversite(String identEtudiant, String codeUniveriste);
}
