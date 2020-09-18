/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.esupportail.esupstage.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.List;
import javax.naming.Name;
import lombok.Data;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

/**
 *
 * @author david
 */
@Data
@Entry(objectClasses = {"supannPerson"})
public class LdapUser implements Serializable {

    @Id
    @JsonIgnore
    private Name dn;

    @Attribute(name = "uid")
    private String login;
    
    @Attribute(name = "sn")
    private String nom;
    
    @Attribute(name = "givenName")
    private String prenom;
    
    @Attribute(name = "mail")
    private String mail;
    
    @Attribute(name = "memberOf")
    private List<String> groupes;    
}
