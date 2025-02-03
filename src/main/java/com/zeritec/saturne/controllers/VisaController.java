package com.zeritec.saturne.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zeritec.saturne.exception.InformationException;
import com.zeritec.saturne.models.Intermediary;
import com.zeritec.saturne.models.RequestResponse;
import com.zeritec.saturne.models.VisaApplication;
import com.zeritec.saturne.services.IntermediaryService;
import com.zeritec.saturne.services.VisaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class VisaController {

	@Autowired
	private VisaService service;
	
	@Autowired
	private IntermediaryService intermediaryService;
	
	@GetMapping("/visas")
	public ResponseEntity<RequestResponse> list() {
		RequestResponse response = new RequestResponse();
		try {
			Iterable<VisaApplication> visas = service.getAll();
			response.setData(visas);
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/visas/{id}")
	public ResponseEntity<RequestResponse> getOne(@PathVariable("id") int id) {
		RequestResponse response = new RequestResponse();
		
		try {
			Optional<VisaApplication> visa = service.getById(id);
			response.setData(visa);
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/visas")
	public ResponseEntity<RequestResponse> create(@Valid @RequestBody VisaApplication visa) {
		RequestResponse response = new RequestResponse();
		
		Optional<Intermediary> inter = intermediaryService.getById(visa.getIntermediaryId());
		if (inter.isPresent()) visa.setIntermediary(inter.get());
		
		try {
			VisaApplication v = service.create(visa);
			response.setData(v);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage(InformationException.getMessageIn(e));
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/visas/{id}")
	public ResponseEntity<RequestResponse> update(@PathVariable("id") int id,@Valid @RequestBody VisaApplication visa) {
		RequestResponse response = new RequestResponse();
		
		try {
			Optional<VisaApplication> r = service.update(id, visa);
			if (r.isPresent()) {
				response.setData(r.get());
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setMessage("Element n existe pas !");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
			
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("/visas/{id}")
	public ResponseEntity<RequestResponse> delete(@PathVariable("id") int id) {
		RequestResponse response = new RequestResponse();
		
		try {
			boolean r = service.delete(id);
			if (r) {
				response.setMessage("Demande d'agrément supprimée !");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setMessage("Element n existe pas !");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
			
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
}
