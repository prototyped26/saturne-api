package com.zeritec.saturne.models;

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
import lombok.Data;

@Entity
@Table(name = "user")
@Data
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String email;
	
	private String password;
	
	private String login;
	
	@Column(name = "first_name")
	@JsonProperty("first_name")
	private String firstName;
	
	@Column(name = "last_name")
	@JsonProperty("last_name")
	private String lastName;
	
	@Column(name = "created_at")
	@JsonProperty("created_at")
	private String createdAt;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "role_id")
	private Role role;
}
