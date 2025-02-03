package com.zeritec.saturne.models.request;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestIntermediary {
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
	private String approvalDate;
	
	@NotNull(message = "Le nom du dirigéant est obligatoire")
	@Column(name = "leader_name")
	@JsonProperty("leader_name")
	private String leaderName;
	
	@Column(name = "approval_number_two")
	@JsonProperty("approval_number_two")
	private String approvalNumberTwo;
	
	@Column(name = "approval_date_two")
	@JsonProperty("approval_date_two")
	private String approvalDateTwo;
	
	@Column(name = "leader_status")
	@JsonProperty("leader_status")
	private String leaderStatus;
	
	private String adress;
	
	private String contacts;
	
	@Column(name = "category_id", insertable = false, updatable = false)
	@NotNull(message = "La Category est obligatoire ")
	@JsonProperty("category_id")
	private Integer categoryId;
	
	@Column(name = "organization_id", insertable = false, updatable = false)
	@JsonProperty("organization_id")
	private Integer organizationId;
}
