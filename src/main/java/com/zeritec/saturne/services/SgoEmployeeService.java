package com.zeritec.saturne.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zeritec.saturne.models.SgoEmployee;
import com.zeritec.saturne.repositories.SgoEmployeeRepository;

@Service
public class SgoEmployeeService {

	@Autowired
	private SgoEmployeeRepository repository;
	
	public Iterable<SgoEmployee> getAll() {
		try {
			return repository.findAll();
		} catch (Exception e) {
			throw new RuntimeException("Erreur récupération liste " + e.getMessage());
		}
	}
	
	public Optional<SgoEmployee> getById(Integer id) {
		
		try {
			return repository.findById(id);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de recherche employee sgo " + e.getMessage());
		}
	}
	
	public Optional<SgoEmployee> getByLabel(String code) {
		
		try {
			return repository.findByLabel(code);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de recherche  d employe sgo " + e.getMessage());
		}
	}
	
	public SgoEmployee create(SgoEmployee data) {
		try {
			return repository.save(data);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de création l employé " + e.getMessage());
		}
	}
	
	public Optional<SgoEmployee> update(Integer id, SgoEmployee data) {
		try {
			
			Optional<SgoEmployee> existing = repository.findById(id);
			if (existing.isPresent()) {
				SgoEmployee toSave = existing.get();
				toSave.setLabel(data.getLabel());
				toSave.setQuality(data.getQuality());
				toSave.setPart(data.getPart());
				toSave.setPercent(data.getPercent());
				toSave.setOpc(data.getOpc());
				
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
			
			Optional<SgoEmployee> existing = repository.findById(id);
			if (existing.isPresent()) {
				SgoEmployee toDelete = existing.get();
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
