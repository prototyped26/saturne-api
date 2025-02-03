package com.zeritec.saturne.models.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RequestCategory {
	@NotEmpty(message = "Un titre est obligatoire")
	private String label;
	
	@NotEmpty(message = "Un Code est obligatoire")
	private String code;
}
