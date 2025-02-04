package com.zeritec.saturne.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zeritec.saturne.models.Year;
import com.zeritec.saturne.repositories.YearRepository;

@Service
public class YearService {

	@Autowired
	private YearRepository repository;
	
	public Iterable<Year> getAll() {
		try {
			return repository.findAll();
		} catch (Exception e) {
			throw new RuntimeException("Erreur récupération liste " + e.getMessage());
		}
	}
	
	public Iterable<Year> actives() {
		try {
			return repository.findByActiveTrue();
		} catch (Exception e) {
			throw new RuntimeException("Erreur récupération liste " + e.getMessage());
		}
	}
	
	public Optional<Year> getById(Integer id) {
		
		try {
			return repository.findById(id);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de recherche catégorie " + e.getMessage());
		}
	}
	
	public Optional<Year> getByCode(String code) {
		
		try {
			return repository.findByCode(code);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de recherche  d action " + e.getMessage());
		}
	}
	
	
	public Iterable<Year> saveAll(Iterable<Year> years) {
		
		try {
			return repository.saveAll(years);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de recherche  de l'année " + e.getMessage());
		}
	}
	
	
	public Year create(Year year) {
		try {
			return repository.save(year);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de création catégorie " + e.getMessage());
		}
	}
	
	public Optional<Year> update(Integer id, Year year) {
		try {
			
			Optional<Year> existing = repository.findById(id);
			if (existing.isPresent()) {
				Year toSave = existing.get();
				toSave.setCode(year.getCode());
				toSave.setLabel(year.getLabel());
				
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
			
			Optional<Year> existing = repository.findById(id);
			if (existing.isPresent()) {
				Year toDelete = existing.get();
				repository.delete(toDelete);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new RuntimeException("Erreur de suppression " + e.getMessage());
		}
	}
}
