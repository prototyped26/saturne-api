package com.zeritec.saturne.models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "visa_application")
@Data
public class VisaApplication {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String label;
	
	private String reason;
	
	private String status;
	
	private String other;
	
	private String decision;
	
	@Column(name = "created_at")
	@JsonProperty("created_at")
	private Date createdAt;
}
