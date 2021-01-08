package fr.esupportail.esupstage.domain.jpa.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import lombok.Getter;
import lombok.Setter;

/**
 * Base information to manage the auditing of an entity.
 *
 * @param <T> the type of the information on the user creating or modifying the entity.
 * @author vdubus
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	@CreatedBy
	@Column(name = "loginCreation")
	private T createdBy;

	@CreatedDate
	@Column(name = "dateCreation")
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private LocalDateTime createdDate;

	@LastModifiedBy
	@Column(name = "loginModif")
	private T modifiedBy;

	@LastModifiedDate
	@Column(name = "dateModif")
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private LocalDateTime modifiedDate;

}
