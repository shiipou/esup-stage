package fr.esupportail.esupstage.services.beans.api.apogee;

import java.util.Date;

public class EtudiantInfoAdm {

	private Integer idEtudiant;

	private String codeSexe;

	private String codeUniversite;

	private Date dateNais;

	private String identEtudiant;

	private String mail;

	private String nom;

	private String nomMarital;

	private String numEtudiant;

	private String numSS;
	
	// libelle CPAM etudiant
	private String libelleCPAM = "";

	private String prenom;

	public Integer getIdEtudiant() {
		return idEtudiant;
	}

	public void setIdEtudiant(Integer idEtudiant) {
		this.idEtudiant = idEtudiant;
	}

	public String getCodeSexe() {
		return codeSexe;
	}

	public void setCodeSexe(String codeSexe) {
		this.codeSexe = codeSexe;
	}

	public String getCodeUniversite() {
		return codeUniversite;
	}

	public void setCodeUniversite(String codeUniversite) {
		this.codeUniversite = codeUniversite;
	}

	public Date getDateNais() {
		return dateNais;
	}

	public void setDateNais(Date dateNais) {
		this.dateNais = dateNais;
	}

	public String getIdentEtudiant() {
		return identEtudiant;
	}

	public void setIdentEtudiant(String identEtudiant) {
		this.identEtudiant = identEtudiant;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getNomMarital() {
		return nomMarital;
	}

	public void setNomMarital(String nomMarital) {
		this.nomMarital = nomMarital;
	}

	public String getNumEtudiant() {
		return numEtudiant;
	}

	public void setNumEtudiant(String numEtudiant) {
		this.numEtudiant = numEtudiant;
	}

	public String getNumSS() {
		return numSS;
	}

	public void setNumSS(String numSS) {
		this.numSS = numSS;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getLibelleCPAM() {
		return libelleCPAM;
	}

	public void setLibelleCPAM(String libelleCPAM) {
		this.libelleCPAM = libelleCPAM;
	}

}
