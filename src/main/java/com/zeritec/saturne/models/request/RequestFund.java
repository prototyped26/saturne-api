package com.zeritec.saturne.models.request;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestFund {
	@NotNull(message = "Le nom du fond est obligatoire")
	private String label;
	
	private String other;
	
	@NotNull(message = "Le numéro d'agrément du fond est obligatoire")
	@Column(name = "approval_number")
	@JsonProperty("approval_number")
	private String approvalNumber;
	
	@NotNull(message = "La date d'agrément du fond est obligatoire")
	@Column(name = "approval_date")
	@JsonProperty("approval_date")
	private String approvalDate;
	
	@Column(name = "distribution_network")
	@JsonProperty("distribution_network")
	private String distributionNetwork;
	
	@NotNull(message = "Le type d'OPC est obligatoire")
	@JsonProperty("type_opc_id")
	private Integer typeOpcId;
	
	@NotNull(message = "Le dépositaire est obligatoire")
	@JsonProperty("depositary_id")
	private Integer depositaryId;
	
	@NotNull(message = "La classification est obligatoire")
	@JsonProperty("classification_id")
	private Integer classificationId;
	
	@NotNull(message = "La distribution est obligatoire")
	@JsonProperty("distribution_id")
	private Integer distributionId;
	
	@NotNull(message = "L'intermédiaire est obligatoire")
	@JsonProperty("intermediary_id")
	private Integer intermediaryId;
	
	@JsonProperty("auditor_id")
	private Integer auditorId;
}
