package com.zeritec.saturne.controllers;

import java.util.ArrayList;
import java.util.List;
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
import com.zeritec.saturne.models.RequestResponse;
import com.zeritec.saturne.models.Week;
import com.zeritec.saturne.models.Year;
import com.zeritec.saturne.models.request.RequestInformation;
import com.zeritec.saturne.services.WeekService;
import com.zeritec.saturne.services.YearService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/system")
public class ManageController {

	@Autowired
	private YearService yearService;
	
	@Autowired
	private WeekService weekService;
	
	@GetMapping("/years")
	public ResponseEntity<RequestResponse> list() {
		RequestResponse response = new RequestResponse();
		try {
			Iterable<Year> years = yearService.getAll();
			response.setData(years);
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/years/actives")
	public ResponseEntity<RequestResponse> listActives() {
		RequestResponse response = new RequestResponse();
		try {
			Iterable<Year> years = yearService.actives();
			response.setData(years);
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/years/{id}")
	public ResponseEntity<RequestResponse> getOne(@PathVariable("id") int id) {
		RequestResponse response = new RequestResponse();
		
		try {
			Optional<Year> year = yearService.getById(id);
			response.setData(year);
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/years")
	public ResponseEntity<RequestResponse> create(@Valid @RequestBody Year year) {
		RequestResponse response = new RequestResponse();
		
		try {
			Year r = yearService.create(year);
			response.setData(r);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage(InformationException.getMessageIn(e));
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/years/{id}")
	public ResponseEntity<RequestResponse> update(@PathVariable("id") int id, @RequestBody Year year) {
		RequestResponse response = new RequestResponse();
		
		try {
			Optional<Year> r = yearService.update(id, year);
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
	
	@PostMapping("/years/{code}/weeks-generate")
	public ResponseEntity<RequestResponse> generateWeeks(@PathVariable("code") String code) {
		RequestResponse response = new RequestResponse();
		
		try {
			Optional<Year> r = yearService.getByCode(code);
			if (r.isPresent()) {
				if (r.get().isGenerated()) {
					response.setMessage("Cette année dispose déjà de semaines");
					return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
				} else {
					List<Week> weeks = weekService.generatedWeekOfYear(Integer.parseInt(r.get().getCode()));
					
					for(Week week: weeks) {
						week.setYear(r.get());
						weekService.create(week);
					}
					
					Year year = r.get();
					year.setGenerated(true);
					yearService.update(year.getId(), year);
					
					response.setMessage("Les semaines de l'année " + r.get().getCode());
					response.setData(weeks);
					return new ResponseEntity<>(response, HttpStatus.OK);
				}
			} else {
				response.setMessage("Element n existe pas !");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
			
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/years/{id}/active")
	public ResponseEntity<RequestResponse> activeYear(@PathVariable("id") int id) {
		RequestResponse response = new RequestResponse();
		
		try {
			Iterable<Year> years = yearService.getAll();
			years.forEach(y -> y.setActive(false));
			yearService.saveAll(years);
			
			Optional<Year> yearOpt = yearService.getById(id);
			if (yearOpt.isEmpty()) {
				response.setMessage("Année inexistante !");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			} else {
				Year year = yearOpt.get();
				year.setActive(true);
				
				Optional<Year> r = yearService.update(id, year);
				if (r.isPresent()) {
					response.setData(r.get());
					return new ResponseEntity<>(response, HttpStatus.OK);
				} else {
					response.setMessage("Element n existe pas !");
					return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
				}
			}
			
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/years/{id}/desactive")
	public ResponseEntity<RequestResponse> desactiveYear(@PathVariable("id") int id) {
		RequestResponse response = new RequestResponse();
		
		Optional<Year> yearOpt = yearService.getById(id);
		if (yearOpt.isEmpty()) {
			response.setMessage("Année inexistante !");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		} else {
			Year year = yearOpt.get();
			year.setActive(false);
			
			try {
				Optional<Year> r = yearService.update(id, year);
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
	}
	
	@DeleteMapping("/years/{id}")
	public ResponseEntity<RequestResponse> delete(@PathVariable("id") int id) {
		RequestResponse response = new RequestResponse();
		
		try {
			boolean r = yearService.delete(id);
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

	@GetMapping("/informations")
	public ResponseEntity<RequestResponse> informations() {
		
		RequestResponse response = new RequestResponse();
        int weekNumber = WeekService.currentWeekNumber(); 
		
		try {
			
			RequestInformation infos = new RequestInformation();
			
			Iterable<Year> actives = yearService.actives();
			for(Year y: actives) {
				infos.setYear(y);
			}
			
			if (infos.getYear() != null) {
				Iterable<Week> weeks = weekService.getWeekOfYear(infos.getYear().getId());
				infos.setWeeks(weeks);
				for(Week w: weeks) {
					if (w.getNumber() == weekNumber) infos.setCurrentWeek(w);
				}
			} else {
				infos.setWeeks(new ArrayList<>());
			}
	        
	        System.out.println("WEEK : " + weekNumber);
			
			response.setData(infos);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
}
