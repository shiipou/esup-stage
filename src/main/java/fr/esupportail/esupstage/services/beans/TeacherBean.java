package fr.esupportail.esupstage.services.beans;

import java.io.Serializable;
import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TeacherBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Include
	@NotBlank
	private String email;

	@NotBlank
	private String firstName;

	@NotBlank
	private String lastName;

	@NotNull
	@DateTimeFormat(iso = ISO.DATE)
	@ApiModelProperty(example = "2018-01-01")
	private LocalDate birthDate;

}
