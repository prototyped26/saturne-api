package com.zeritec.saturne.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "holder")
@Data
public class Holder {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "first_name")
	@JsonProperty("first_name")
	private String firstName;
	
	@Column(name = "last_name")
	@JsonProperty("last_name")
	private String lastName;
	
	private String shares;
	
	private String status;
}
