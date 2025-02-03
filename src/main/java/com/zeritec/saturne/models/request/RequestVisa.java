package com.zeritec.saturne.models.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestVisa {

	@NotNull
	@Size(min = 5, max = 300, message = "Cette information est obligatoire")
	private String label;
	@NotNull
	@Size(min = 5, max = 300, message = "La raison d'obtention de l'agrément  est obligatoire")
	private String reason;
	
	private String status;
	
	private String other;
	
	private String decision;
	
	private String created_at;
	
	private int intermediary_id;
}
