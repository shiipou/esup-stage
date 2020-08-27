package fr.esupportail.esupstage.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
    @Table(name = "enseignants")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public class Enseignant {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;

        @NotEmpty
        @Size(min = 6, max = 20)
        @Column(name = "username")
        private String username;

        @NotEmpty
        @Column(name = "email")
        private String email;

        @NotEmpty
        @Column(name = "first_name")
        private String firstname;

        @NotEmpty
        @Column(name = "last_name")
        private String lastname;

        @Column(name = "birth_date")
        @NotNull
        private Date birthDate;
    }

