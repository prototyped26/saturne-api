package com.zeritec.saturne.services;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zeritec.saturne.models.Week;
import com.zeritec.saturne.repositories.WeekRepository;

@Service
public class WeekService {

	@Autowired
	private WeekRepository repository;
	
	public Iterable<Week> getAll() {
		try {
			return repository.findAll();
		} catch (Exception e) {
			throw new RuntimeException("Erreur récupération liste " + e.getMessage());
		}
	}
	
	public Iterable<Week> getWeekOfYear(int yearId) {
		try {
			return repository.findByYearId(yearId);
		} catch (Exception e) {
			throw new RuntimeException("Erreur récupération liste " + e.getMessage());
		}
	}
	
	public Optional<Week> getById(Integer id) {
		
		try {
			return repository.findById(id);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de recherche catégorie " + e.getMessage());
		}
	}
	
	public Optional<Week> getByNumber(int nbr) {
		
		try {
			return repository.findByNumber(nbr);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de recherche  d semaine " + e.getMessage());
		}
	}
	
	public Optional<Week> getByLabel(String code) {
		
		try {
			return repository.findByLabel(code);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de recherche  de semaine " + e.getMessage());
		}
	}
	
	public Week create(Week week) {
		try {
			return repository.save(week);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de création de la semaine " + e.getMessage());
		}
	}
	
	public Optional<Week> update(Integer id, Week week) {
		try {
			
			Optional<Week> existing = repository.findById(id);
			if (existing.isPresent()) {
				Week toSave = existing.get();
				toSave.setNumber(week.getNumber());
				toSave.setLabel(week.getLabel());
				toSave.setMonth(week.getMonth());
				toSave.setStart(week.getStart());
				toSave.setEnd(week.getEnd());
				
				return Optional.of(repository.save(toSave));
			} else {
				return Optional.empty();
			}
			
		} catch (Exception e) {
			throw new RuntimeException("Erreur de mise à jour" + e.getMessage());
		}
	}
	
	public boolean delete(Integer id) {
		try {
			
			Optional<Week> existing = repository.findById(id);
			if (existing.isPresent()) {
				Week toDelete = existing.get();
				repository.delete(toDelete);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new RuntimeException("Erreur de suppression " + e.getMessage());
		}
	}
	
	public List<Week> generatedWeekOfYear(int year) {
	    LocalDate date = LocalDate.of(year, 1, 1); // Java 8 date format yyyy MH d
	    List<Week> weeks = new ArrayList<>();
	    
	    int initialWeekOfyear = date.get(WeekFields.of(Locale.UK).weekOfWeekBasedYear());
	    int weekOfYear = initialWeekOfyear;
	    do {
	        LocalDate firstDayOfWeek = date.with(WeekFields.of(Locale.UK).dayOfWeek(), 2L);
	        LocalDate lastWorkingDayOfWeek = date.with(WeekFields.of(Locale.UK).dayOfWeek(), 6L);
	        
	        Week week = new Week();
	        week.setLabel("Semaine " + weekOfYear);
	        week.setNumber(weekOfYear);
	        week.setStart( Date.from(firstDayOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant()) );
	        week.setEnd(Date.from(lastWorkingDayOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant()) );

	        weeks.add(week);
	        date = date.plusWeeks(1);
	    } while ((weekOfYear = date.get(WeekFields.of(Locale.UK).weekOfWeekBasedYear())) != initialWeekOfyear);
	    
	    return weeks;
	}
}
