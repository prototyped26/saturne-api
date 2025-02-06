package com.zeritec.saturne.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import com.zeritec.saturne.models.Classification;
import com.zeritec.saturne.models.Depositary;
import com.zeritec.saturne.models.Distribution;
import com.zeritec.saturne.models.Fund;
import com.zeritec.saturne.models.Intermediary;
import com.zeritec.saturne.models.RequestResponse;
import com.zeritec.saturne.models.TypeOpc;
import com.zeritec.saturne.models.request.RequestFund;
import com.zeritec.saturne.services.ClassificationService;
import com.zeritec.saturne.services.DepositaryService;
import com.zeritec.saturne.services.DistributionService;
import com.zeritec.saturne.services.FundService;
import com.zeritec.saturne.services.IntermediaryService;
import com.zeritec.saturne.services.TypeOpcService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class FundController {

	@Autowired
	private FundService service;
	@Autowired
	private TypeOpcService typeOpcService;
	@Autowired
	private DepositaryService depositaryService;
	@Autowired
	private ClassificationService classificationService;
	@Autowired
	private DistributionService distributionService;
	@Autowired
	private IntermediaryService intermediaryService;
	
	@GetMapping("/funds")
	public ResponseEntity<RequestResponse> list() {
		RequestResponse response = new RequestResponse();
		try {
			Iterable<Fund> data = service.getAll();
			response.setData(data);
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/funds/{id}")
	public ResponseEntity<RequestResponse> getOne(@PathVariable("id") int id) {
		RequestResponse response = new RequestResponse();
		
		try {
			Optional<Fund> data = service.getById(id);
			if (data.isEmpty()) {
				response.setMessage("Fond non existant");
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
	
	@PostMapping("/funds")
	public ResponseEntity<RequestResponse> create(@Valid @RequestBody RequestFund req) {
		RequestResponse response = new RequestResponse();
		
		Fund fund = service.convertRequest(req);
		fund.setApprovalNumber(service.normalizeApprovalNumber(fund.getApprovalNumber()));
		
		Optional<TypeOpc> topc = typeOpcService.getById(req.getTypeOpcId());
		if (topc.isEmpty()) {
			response.setMessage("Type d'OPC inconnu, vérifier !");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		} else {
			fund.setTypeOpc(topc.get());
		}
		
		Optional<Depositary> dep = depositaryService.getById(req.getDepositaryId());
		if (dep.isEmpty()) {
			response.setMessage("Dépositaire inconnu, vérifier !");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		} else {
			fund.setDepositary(dep.get());
		}
		
		Optional<Classification> classif = classificationService.getById(req.getClassificationId());
		if (classif.isEmpty()) {
			response.setMessage("Classification inconnu, vérifier !");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		} else {
			fund.setClassification(classif.get());
		}
		
		Optional<Distribution> dist = distributionService.getById(req.getDistributionId());
		if (dist.isEmpty()) {
			response.setMessage("Distribution inconnu, vérifier !");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		} else {
			fund.setDistribution(dist.get());
		}
		
		Optional<Intermediary> inter = intermediaryService.getById(req.getIntermediaryId());
		if (inter.isEmpty()) {
			response.setMessage("Intermédiaire inconnu, vérifier !");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		} else {
			fund.setIntermediary(inter.get());
		}
		
		if (req.getAuditorId() != null) {
			Optional<Intermediary> org = intermediaryService.getById(req.getAuditorId());
			if (org.isPresent()) fund.setAuditor(org.get());
		}
		
		try {
			Fund d = service.create(fund);
			response.setData(d);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage(InformationException.getMessageIn(e));
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/funds/{id}")
	public ResponseEntity<RequestResponse> update(@PathVariable("id") int id,@Valid @RequestBody RequestFund req) {
		RequestResponse response = new RequestResponse();
		
		Fund fund = service.convertRequest(req);
		
		Optional<TypeOpc> topc = typeOpcService.getById(req.getTypeOpcId());
		if (topc.isEmpty()) {
			response.setMessage("Type d'OPC inconnu, vérifier !");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		} else {
			fund.setTypeOpc(topc.get());
		}
		
		Optional<Depositary> dep = depositaryService.getById(req.getDepositaryId());
		if (dep.isEmpty()) {
			response.setMessage("Dépositaire inconnu, vérifier !");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		} else {
			fund.setDepositary(dep.get());
		}
		
		Optional<Classification> classif = classificationService.getById(req.getClassificationId());
		if (classif.isEmpty()) {
			response.setMessage("Classification inconnu, vérifier !");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		} else {
			fund.setClassification(classif.get());
		}
		
		Optional<Distribution> dist = distributionService.getById(req.getDistributionId());
		if (dist.isEmpty()) {
			response.setMessage("Distribution inconnu, vérifier !");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		} else {
			fund.setDistribution(dist.get());
		}
		
		Optional<Intermediary> inter = intermediaryService.getById(req.getIntermediaryId());
		if (inter.isEmpty()) {
			response.setMessage("Intermédiaire inconnu, vérifier !");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		} else {
			fund.setIntermediary(inter.get());
		}
		
		if (req.getAuditorId() != null) {
			Optional<Intermediary> org = intermediaryService.getById(req.getAuditorId());
			if (org.isPresent()) fund.setAuditor(org.get());
		}
		
		try {
			Optional<Fund> r = service.update(id, fund);
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
	
	@DeleteMapping("/funds/{id}")
	public ResponseEntity<RequestResponse> delete(@PathVariable("id") int id) {
		RequestResponse response = new RequestResponse();
		
		try {
			boolean r = service.delete(id);
			if (r) {
				response.setMessage("Element supprimé !");
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
	
	@PostMapping("/funds/import")
	public ResponseEntity<RequestResponse> importFunds(@RequestParam("file") MultipartFile file) throws IOException {
		RequestResponse response = new RequestResponse();
		List<Fund> funds = new ArrayList<>();
		
		Workbook workbook = new XSSFWorkbook(file.getInputStream());
		for(int i=1 ; i < workbook.getSheetAt(0).getPhysicalNumberOfRows(); i++) {
			Fund fund = new Fund();
			Row row = workbook.getSheetAt(0).getRow(i);
			
			Optional<Intermediary> sgo = intermediaryService.getByLabel(row.getCell(1).getStringCellValue());
			if (sgo.isPresent()) {
				fund.setIntermediary(sgo.get());
			}
			
			Optional<TypeOpc> typeOpc = typeOpcService.getByLabel(row.getCell(3).getStringCellValue());
			if (typeOpc.isPresent()) {
				fund.setTypeOpc(typeOpc.get());
			}
			
			Optional<Depositary> depositary = depositaryService.getByLabel(row.getCell(6).getStringCellValue());
			if (depositary.isPresent()) {
				fund.setDepositary(depositary.get());
			}
			
			Optional<Classification> classific = classificationService.getByLabel(row.getCell(7).getStringCellValue());
			if (classific.isPresent()) {
				fund.setClassification(classific.get());
			}
			
			Optional<Distribution> dist = distributionService.getByLabel(row.getCell(7).getStringCellValue());
			if (dist.isPresent()) {
				fund.setDistribution(dist.get());
			}
			
			fund.setLabel(row.getCell(2).getStringCellValue());
			fund.setApprovalNumber(row.getCell(4).getStringCellValue());
			fund.setApprovalDate(row.getCell(5).getDateCellValue());
			
			funds.add(fund);
		}
		workbook.close();
		
		List<Fund> res =  new ArrayList<>();
		
		for(Fund f: funds) {
			try {
				Fund r = service.create(f);
				res.add(r);
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e.getMessage());
			}
		}
		
		response.setData(funds);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/funds/types-opc")
	public ResponseEntity<RequestResponse> listTypesOpc() {
		RequestResponse response = new RequestResponse();
		try {
			Iterable<TypeOpc> data = typeOpcService.getAll();
			response.setData(data);
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	} 
	
	@GetMapping("/funds/depositaries")
	public ResponseEntity<RequestResponse> listDepositaries() {
		RequestResponse response = new RequestResponse();
		try {
			Iterable<Depositary> data = depositaryService.getAll();
			response.setData(data);
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/funds/classifications")
	public ResponseEntity<RequestResponse> listClassification() {
		RequestResponse response = new RequestResponse();
		try {
			Iterable<Classification> data = classificationService.getAll();
			response.setData(data);
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/funds/distritubtions")
	public ResponseEntity<RequestResponse> listDistributions() {
		RequestResponse response = new RequestResponse();
		try {
			Iterable<Distribution> data = distributionService.getAll();
			response.setData(data);
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
}
