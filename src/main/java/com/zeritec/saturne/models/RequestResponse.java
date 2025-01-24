package com.zeritec.saturne.models;

import lombok.Data;

@Data
public class RequestResponse {
	public String message;
	
	public int status;
	
	public int code;
	
	public Object data;
	
	public Object none;
}
