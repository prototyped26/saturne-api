package com.zeritec.saturne.models;

import java.util.List;

import lombok.Data;

@Data
public class RequestResponse {
	public String message;
	
	public int status;
	
	public List<String> errors;
	
	public Object data;
}
