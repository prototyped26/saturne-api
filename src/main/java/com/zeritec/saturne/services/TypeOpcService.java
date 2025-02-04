package com.zeritec.saturne.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zeritec.saturne.models.TypeOpc;
import com.zeritec.saturne.repositories.TypeOPCRepository;

@Service
public class TypeOpcService {

	@Autowired
	private TypeOPCRepository repository;
	
	public Iterable<TypeOpc> getAll() {
		try {
			return repository.findAll();
		} catch (Exception e) {
			throw new RuntimeException("Erreur récupération liste " + e.getMessage());
		}
	}
	
	public Optional<TypeOpc> getById(Integer id) {
		
		try {
			return repository.findById(id);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de recherche catégorie " + e.getMessage());
		}
	}
	
	public Optional<TypeOpc> getByCode(String code) {
		
		try {
			return repository.findByCode(code);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de recherche  d action " + e.getMessage());
		}
	}
	
	public Optional<TypeOpc> getByLabel(String code) {
		
		try {
			return repository.findByLabel(code);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de recherche  d action " + e.getMessage());
		}
	}
	
	public TypeOpc create(TypeOpc data) {
		try {
			return repository.save(data);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de création catégorie " + e.getMessage());
		}
	}
	
	public Optional<TypeOpc> update(Integer id, TypeOpc data) {
		try {
			
			Optional<TypeOpc> existing = repository.findById(id);
			if (existing.isPresent()) {
				TypeOpc toSave = existing.get();
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
			
			Optional<TypeOpc> existing = repository.findById(id);
			if (existing.isPresent()) {
				TypeOpc toDelete = existing.get();
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
