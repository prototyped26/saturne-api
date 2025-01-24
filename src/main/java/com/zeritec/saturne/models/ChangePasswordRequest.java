package com.zeritec.saturne.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ChangePasswordRequest {
	@JsonProperty("old_password")
	private String oldPassword;
	
	@JsonProperty("new_password")
	private String newPassword;
	
	@JsonProperty("confirm_password")
	private String confirmPassword;
}
