package com.zeritec.saturne.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "year")
@Data
public class Year {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotNull(message = "Le label de l'année est obligatoire")
	private String label;
	@NotNull(message = "Le code de l'année est obligatoire")
	private String code;
	
	private boolean active;
	
	private boolean generated;
}
