package com.zeritec.saturne.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zeritec.saturne.models.OpcvmType;
import com.zeritec.saturne.repositories.OpcvmTypeRepository;

@Service
public class OpcvmTypeService {

	@Autowired
	private OpcvmTypeRepository repository;
	
	public Iterable<OpcvmType> getAll() {
		try {
			return repository.findAll();
		} catch (Exception e) {
			throw new RuntimeException("Erreur récupération liste " + e.getMessage());
		}
	}
	
	public Optional<OpcvmType> getById(Integer id) {
		
		try {
			return repository.findById(id);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de recherche catégorie " + e.getMessage());
		}
	}
	
	public Optional<OpcvmType> getByCode(String code) {
		
		try {
			return repository.findByCode(code);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de recherche  d action " + e.getMessage());
		}
	}
	
	public OpcvmType create(OpcvmType data) {
		try {
			return repository.save(data);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de création catégorie " + e.getMessage());
		}
	}
	
	public Optional<OpcvmType> update(Integer id, OpcvmType data) {
		try {
			
			Optional<OpcvmType> existing = repository.findById(id);
			if (existing.isPresent()) {
				OpcvmType toSave = existing.get();
				toSave.setCode(data.getCode());
				toSave.setLabel(data.getLabel());
				
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
			
			Optional<OpcvmType> existing = repository.findById(id);
			if (existing.isPresent()) {
				OpcvmType toDelete = existing.get();
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
