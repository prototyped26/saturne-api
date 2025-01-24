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
@Table(name = "fund")
@Data
public class Fund {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String label;
	
	private String other;
	
	@Column(name = "approval_number")
	@JsonProperty("approval_number")
	private String approvalNumber;
	
	@Column(name = "approval_date")
	@JsonProperty("approval_date")
	private Date approvalDate;
	
	@Column(name = "distribution_network")
	@JsonProperty("distribution_network")
	private String distributionNetwork;
}
