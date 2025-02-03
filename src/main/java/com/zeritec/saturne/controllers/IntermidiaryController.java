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
import com.zeritec.saturne.models.Category;
import com.zeritec.saturne.models.Intermediary;
import com.zeritec.saturne.models.Organization;
import com.zeritec.saturne.models.RequestResponse;
import com.zeritec.saturne.models.request.RequestCategory;
import com.zeritec.saturne.models.request.RequestIntermediary;
import com.zeritec.saturne.services.CategoryService;
import com.zeritec.saturne.services.IntermediaryService;
import com.zeritec.saturne.services.OrganizationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class IntermidiaryController {
	
	@Autowired
	private IntermediaryService service;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private OrganizationService organizationService;
	
	@GetMapping("/intermediaries")
	public ResponseEntity<RequestResponse> list() {
		RequestResponse response = new RequestResponse();
		try {
			Iterable<Intermediary> data = service.getAll();
			response.setData(data);
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/intermediaries/{id}")
	public ResponseEntity<RequestResponse> getOne(@PathVariable("id") int id) {
		RequestResponse response = new RequestResponse();
		
		try {
			Optional<Intermediary> data = service.getById(id);
			if (data.isEmpty()) {
				response.setMessage("Intermédiaire non existant");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			} else {
				response.setData(data.get());
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/intermediaries")
	public ResponseEntity<RequestResponse> create(@Valid @RequestBody RequestIntermediary req) {
		RequestResponse response = new RequestResponse();
		
		Intermediary inter = service.convertRequest(req);
		
		Optional<Category> catOpt = categoryService.getById(req.getCategoryId());
		if (catOpt.isEmpty()) {
			response.setMessage("Catégorie inconnue, vérifier !");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		} else {
			inter.setCategory(catOpt.get());
		}
		
		if (req.getOrganizationId() != null) {
			Optional<Organization> org = organizationService.getById(req.getOrganizationId());
			if (org.isPresent()) inter.setOrganization(org.get());
		}
		
		try {
			Intermediary d = service.create(inter);
			response.setData(d);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage(InformationException.getMessageIn(e));
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/intermediaries/{id}")
	public ResponseEntity<RequestResponse> update(@PathVariable("id") int id,@Valid @RequestBody RequestIntermediary req) {
		RequestResponse response = new RequestResponse();
		Intermediary inter = service.convertRequest(req);
		
		Optional<Category> catOpt = categoryService.getById(req.getCategoryId());
		if (catOpt.isEmpty()) {
			response.setMessage("Catégorie inconnue, vérifier !");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		} else {
			inter.setCategory(catOpt.get());
		}
		
		if (req.getOrganizationId() != null) {
			Optional<Organization> org = organizationService.getById(req.getOrganizationId());
			if (org.isPresent()) inter.setOrganization(org.get());
		}
		
		try {
			Optional<Intermediary> r = service.update(id, inter);
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
	
	
	@DeleteMapping("/intermediaries/{id}")
	public ResponseEntity<RequestResponse> delete(@PathVariable("id") int id) {
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
	 * Categories routes
	 */
	@GetMapping("/intermediaries/categories")
	public ResponseEntity<RequestResponse> listCategories() {
		RequestResponse response = new RequestResponse();
		try {
			Iterable<Category> data = categoryService.getAll();
			response.setData(data);
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/intermediaries/categories/{id}")
	public ResponseEntity<RequestResponse> getCategory(@PathVariable("id") int id) {
		RequestResponse response = new RequestResponse();
		
		try {
			Optional<Category> data = categoryService.getById(id);
			response.setData(data);
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/intermediaries/categories")
	public ResponseEntity<RequestResponse> createCategoryn(@Valid @RequestBody RequestCategory cat) {
		RequestResponse response = new RequestResponse();
			
		try {
			Category d = categoryService.create(categoryService.convertRequest(cat));
			response.setData(d);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage(InformationException.getMessageIn(e));
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/intermediaries/categories/{id}")
	public ResponseEntity<RequestResponse> updateCategory(@PathVariable("id") int id,@Valid @RequestBody RequestCategory cat) {
		RequestResponse response = new RequestResponse();
		
		try {
			Optional<Category> r = categoryService.update(id, categoryService.convertRequest(cat));
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
	
	
	@DeleteMapping("/intermediaries/categories/{id}")
	public ResponseEntity<RequestResponse> deleteCategory(@PathVariable("id") int id) {
		RequestResponse response = new RequestResponse();
		
		try {
			boolean r = categoryService.delete(id);
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
