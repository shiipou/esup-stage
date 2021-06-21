package fr.esupportail.esupstage.services.beans.conventions;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class APIStudentBean implements Serializable {

	private static final long serialVersionUID = 9201637130740729137L;

	private String uid;

	private String codEtu;

	private List<String> sn;

	private String mail;

	private String supannAutreMail;

	private String cn;

	private List<String> givenName;

	private String displayName;

	private String eduPersonPrimaryAffiliation;

	private List<String> eduPersonAffiliation;
}
