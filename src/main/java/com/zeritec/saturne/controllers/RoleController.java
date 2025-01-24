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

import com.zeritec.saturne.models.RequestResponse;
import com.zeritec.saturne.models.Role;
import com.zeritec.saturne.services.RoleService;

@RestController
@RequestMapping("/api")
public class RoleController {

	@Autowired
	private RoleService service;
	
	@GetMapping("/roles")
	public ResponseEntity<RequestResponse> list() {
		RequestResponse response = new RequestResponse();
		try {
			Iterable<Role> roles = service.getAll();
			response.setData(roles);
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(HttpStatus.BAD_REQUEST.value());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/roles/{id}")
	public ResponseEntity<RequestResponse> getOne(@PathVariable("id") int id) {
		RequestResponse response = new RequestResponse();
		
		try {
			Optional<Role> role = service.getById(id);
			response.setData(role);
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(HttpStatus.BAD_REQUEST.value());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/roles")
	public ResponseEntity<RequestResponse> create(@RequestBody Role role) {
		RequestResponse response = new RequestResponse();
		
		try {
			Role r = service.create(role);
			response.setData(r);
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(HttpStatus.BAD_REQUEST.value());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/roles/{id}")
	public ResponseEntity<RequestResponse> update(@PathVariable("id") int id, @RequestBody Role role) {
		RequestResponse response = new RequestResponse();
		
		try {
			Optional<Role> r = service.update(id, role);
			if (r.isPresent()) {
				response.setData(r.get());
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setMessage("Element n existe pas !");
				response.setCode(HttpStatus.BAD_REQUEST.value());
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
			
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(HttpStatus.BAD_REQUEST.value());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("/roles/{id}")
	public ResponseEntity<RequestResponse> delete(@PathVariable("id") int id) {
		RequestResponse response = new RequestResponse();
		
		try {
			boolean r = service.delete(id);
			if (r) {
				response.setMessage("element supprimé !");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setMessage("Element n existe pas !");
				response.setCode(HttpStatus.BAD_REQUEST.value());
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
			
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setCode(HttpStatus.BAD_REQUEST.value());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
}
