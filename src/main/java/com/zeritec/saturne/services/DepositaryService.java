package com.zeritec.saturne.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zeritec.saturne.models.Depositary;
import com.zeritec.saturne.repositories.DepositaryRepository;

@Service
public class DepositaryService {

	@Autowired
	private DepositaryRepository repository;
	
	public Iterable<Depositary> getAll() {
		try {
			return repository.findAll();
		} catch (Exception e) {
			throw new RuntimeException("Erreur récupération liste " + e.getMessage());
		}
	}
	
	public Optional<Depositary> getById(Integer id) {
		
		try {
			return repository.findById(id);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de recherche catégorie " + e.getMessage());
		}
	}
	
	public Optional<Depositary> getByCode(String code) {
		
		try {
			return repository.findByCode(code);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de recherche  d action " + e.getMessage());
		}
	}
	
	public Depositary create(Depositary data) {
		try {
			return repository.save(data);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de création catégorie " + e.getMessage());
		}
	}
	
	public Optional<Depositary> update(Integer id, Depositary data) {
		try {
			
			Optional<Depositary> existing = repository.findById(id);
			if (existing.isPresent()) {
				Depositary toSave = existing.get();
				toSave.setCode(data.getCode());
				toSave.setLabel(data.getLabel());
				toSave.setDescription(data.getDescription());
				
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
			
			Optional<Depositary> existing = repository.findById(id);
			if (existing.isPresent()) {
				Depositary toDelete = existing.get();
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
