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
import com.zeritec.saturne.models.Holder;
import com.zeritec.saturne.models.Organization;
import com.zeritec.saturne.models.RequestResponse;
import com.zeritec.saturne.services.HolderService;
import com.zeritec.saturne.services.OrganizationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class OrganizationController {

	@Autowired
	private OrganizationService service;
	
	@Autowired
	private HolderService holderService;
	
	@GetMapping("/intermediaries/organizations")
	public ResponseEntity<RequestResponse> listOrganizations() {
		RequestResponse response = new RequestResponse();
		try {
			Iterable<Organization> data = service.getAll();
			response.setData(data);
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/intermediaries/organizations/{id}")
	public ResponseEntity<RequestResponse> getOrganization(@PathVariable("id") int id) {
		RequestResponse response = new RequestResponse();
		
		try {
			Optional<Organization> data = service.getById(id);
			if (data.isEmpty()) {
				response.setMessage("L'organisation n existe pas");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			} else {
				response.setData(data.get());
			}
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/intermediaries/organizations")
	public ResponseEntity<RequestResponse> createOrganization(@Valid @RequestBody Organization org) {
		RequestResponse response = new RequestResponse();
			
		try {
			Organization d = service.create(org);
			response.setData(d);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage(InformationException.getMessageIn(e));
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/intermediaries/organizations/{id}")
	public ResponseEntity<RequestResponse> updateOrganization(@PathVariable("id") int id,@Valid @RequestBody Organization org) {
		RequestResponse response = new RequestResponse();
		
		try {
			Optional<Organization> r = service.update(id, org);
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
	
	
	@DeleteMapping("/intermediaries/organizations/{id}")
	public ResponseEntity<RequestResponse> deleteOrganization(@PathVariable("id") int id) {
		RequestResponse response = new RequestResponse();
		
		try {
			boolean r = service.delete(id);
			if (r) {
				response.setMessage("element supprimé !");
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
	
	/**
	 * Holder routes 
	 */
	@GetMapping("/holders")
	public ResponseEntity<RequestResponse> listHolders() {
		RequestResponse response = new RequestResponse();
		try {
			Iterable<Holder> data = holderService.getAll();
			response.setData(data);
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/holders/{id}")
	public ResponseEntity<RequestResponse> getHolder(@PathVariable("id") int id) {
		RequestResponse response = new RequestResponse();
		
		try {
			Optional<Holder> data = holderService.getById(id);
			response.setData(data);
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/holders")
	public ResponseEntity<RequestResponse> createHolder(@Valid @RequestBody Holder o) {
		RequestResponse response = new RequestResponse();
		
		Optional<Organization> org = service.getById(o.getOrganizationId());
		if (org.isEmpty()) {
			response.setMessage("Il faut une organisation, veuillez vérifier");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		} else {
			o.setOrganization(org.get());
		}
			
		try {
			Holder d = holderService.create(o);
			response.setData(d);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage(InformationException.getMessageIn(e));
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/holders/{id}")
	public ResponseEntity<RequestResponse> updateHolder(@PathVariable("id") int id,@Valid @RequestBody Holder o) {
		RequestResponse response = new RequestResponse();
		
		Optional<Organization> org = service.getById(o.getOrganizationId());
		if (org.isEmpty()) {
			response.setMessage("Il faut une organisation, veuillez vérifier");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		} else {
			o.setOrganization(org.get());
		}
		
		try {
			Optional<Holder> r = holderService.update(id, o);
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
	
	
	@DeleteMapping("/holders/{id}")
	public ResponseEntity<RequestResponse> deleteHolder(@PathVariable("id") int id) {
		RequestResponse response = new RequestResponse();
		
		try {
			boolean r = holderService.delete(id);
			if (r) {
				response.setMessage("element supprimé !");
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
