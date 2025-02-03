package com.zeritec.saturne.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zeritec.saturne.models.VisaApplication;
import com.zeritec.saturne.repositories.VisaRepository;

@Service
public class VisaService {

	@Autowired
	private VisaRepository repository;
	
	public Iterable<VisaApplication> getAll() {
		try {
			return repository.findAll();
		} catch (Exception e) {
			throw new RuntimeException("Erreur lors de la lecture de la liste  des demandes " + e.getMessage());
		}
	}
	
	public Optional<VisaApplication> getById(Integer id) {
		
		try {
			return repository.findById(id);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de recherche  de la demande " + e.getMessage());
		}
	}
	
	public Optional<VisaApplication> getByLabel(String code) {
		
		try {
			return repository.findByLabel(code);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de recherche  de la demande " + e.getMessage());
		}
	}
	
	public VisaApplication create(VisaApplication visa) {
		try {
			return repository.save(visa);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de création d'une demande " + e.getMessage());
		}
	}
	
	public Optional<VisaApplication> update(Integer id, VisaApplication visa) {
		try {
			
			Optional<VisaApplication> existing = repository.findById(id);
			if (existing.isPresent()) {
				VisaApplication toSave = existing.get();
				toSave.setLabel(visa.getLabel());
				toSave.setReason(visa.getReason());
				toSave.setStatus(visa.getStatus());
				toSave.setOther(visa.getOther());
				toSave.setDecision(visa.getDecision());
				toSave.setCreatedAt(visa.getCreatedAt());
				if (visa.getIntermediary() != null) toSave.setIntermediary(visa.getIntermediary());
				
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
			
			Optional<VisaApplication> existing = repository.findById(id);
			if (existing.isPresent()) {
				VisaApplication toDelete = existing.get();
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
