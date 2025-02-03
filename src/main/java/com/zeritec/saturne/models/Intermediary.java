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
@Table(name = "intermediary")
@Data
public class Intermediary {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotNull
	@Size(min = 3, max = 256, message = "La désignation est obligatoire")
	private String label;
	
	@NotNull
	@Size(min = 3, max = 256, message = "Le siège est obligatoire")
	private String head;
	
	@NotNull
	@Size(min = 10, max = 256, message = "Le Numéro d'agrément est obligatoire")
	@Column(name = "approval_number")
	@JsonProperty("approval_number")
	private String approvalNumber;
	
	@NotNull(message = "La date d'agrément est obligatoire")
	@Column(name = "approval_date")
	@JsonProperty("approval_date")
	private Date approvalDate;
	
	@NotNull(message = "Le nom du dirigéant est obligatoire")
	@Column(name = "leader_name")
	@JsonProperty("leader_name")
	private String leaderName;
	
	@Column(name = "approval_number_two")
	@JsonProperty("approval_number_two")
	private String approvalNumberTwo;
	
	@Column(name = "approval_date_two")
	@JsonProperty("approval_date_two")
	private Date approvalDateTwo;
	
	@Column(name = "leader_status")
	@JsonProperty("leader_status")
	private String leaderStatus;
	
	private String adress;
	
	private String contacts;
	
	@NotNull(message = "La Category est obligatoire ")
	@JsonProperty("category_id")
	private int categoryId;
	
	@JsonProperty("organization_id")
	private int organizationId;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "category_id")
	private Category category;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "organization_id")
	private Organization organization;
}
