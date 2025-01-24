package com.zeritec.saturne.models;

import lombok.Data;

@Data
public class LoginRequest {
	private String email;
	
	private String password;
}
