package com.zeritec.saturne.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
import com.zeritec.saturne.utils.DateUtils;

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

	@PostMapping("/intermediaries/import")
	public ResponseEntity<RequestResponse> importData(@RequestParam("file") MultipartFile file) throws IOException {
		RequestResponse response = new RequestResponse();
		
		List<Intermediary> intermediaries = new ArrayList<>();
		
		Workbook workbook = new XSSFWorkbook(file.getInputStream());
		
		for(int i=2 ; i < workbook.getSheetAt(0).getPhysicalNumberOfRows(); i++) {
			Row row = workbook.getSheetAt(0).getRow(i);
			
			Intermediary inter = new Intermediary();
			
			Optional<Category> cat = categoryService.getByLabel(row.getCell(0).getStringCellValue());
			if (cat.isPresent()) {
				inter.setCategory(cat.get());
			}
		
			inter.setLabel(row.getCell(1).getStringCellValue());
			inter.setHead(row.getCell(2).getStringCellValue());
			inter.setApprovalNumber(row.getCell(3).getStringCellValue());
			inter.setApprovalDate(row.getCell(4).getDateCellValue());
			inter.setLeaderName(row.getCell(5).getStringCellValue());
			inter.setLeaderStatus(row.getCell(6).getStringCellValue());
			inter.setApprovalNumberTwo(row.getCell(7).getStringCellValue());
			inter.setApprovalDateTwo(row.getCell(8).getDateCellValue());
			inter.setAdress(row.getCell(9).getStringCellValue().trim().replaceAll("\\s{2,}", " "));
			inter.setContacts(row.getCell(10).getStringCellValue().trim().replaceAll("\\s{2,}", " "));
			
			intermediaries.add(inter);
		}
		
		workbook.close();
		
		List<Intermediary> saves = new ArrayList<>();
		for(Intermediary intermediary: intermediaries) {
			try {
				Intermediary s = service.create(intermediary);
				saves.add(s);
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e.getMessage());
			}
		}
		
		response.setData(saves);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
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
