package com.zeritec.saturne.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RegisterRequest {
	@JsonProperty("last_name")
	private String lastName;
	
	@JsonProperty("first_name")
	private String firstName;
	
	private String email;
	
	private String login;
	
	private String password;
	
	@JsonProperty("role_id")
	private Integer roleId;
}
