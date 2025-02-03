package com.zeritec.saturne.models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "visa_application")
@Data
public class VisaApplication {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@NotNull
	@Size(min = 5, max = 300, message = "Informations sur l'obtention de l'agrément  est obligatoire")
	private String label;
	@NotNull
	@Size(min = 5, max = 300, message = "La raison d'obtention de l'agrément  est obligatoire")
	private String reason;
	
	private String status;
	
	private String other;
	
	private String decision;
	
	@Column(name = "created_at")
	@JsonProperty("created_at")
	private Date createdAt;
	
	@JsonProperty("intermediary_id")
	private int intermediaryId;
	
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinColumn(name = "intermediary_id") 
	private Intermediary intermediary;
}
