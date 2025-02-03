package com.zeritec.saturne.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zeritec.saturne.models.Holder;
import com.zeritec.saturne.repositories.HolderRepository;

@Service
public class HolderService {

	@Autowired
	private HolderRepository repository;
	
	public Iterable<Holder> getAll() {
		try {
			return repository.findAll();
		} catch (Exception e) {
			throw new RuntimeException("Erreur récupération liste " + e.getMessage());
		}
	}
	
	public Optional<Holder> getById(Integer id) {
		
		try {
			return repository.findById(id);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de recherche catégorie " + e.getMessage());
		}
	}
	
	public Holder create(Holder holder) {
		try {
			return repository.save(holder);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de création catégorie " + e.getMessage());
		}
	}
	
	public Optional<Holder> update(Integer id, Holder holder) {
		try {
			
			Optional<Holder> existing = repository.findById(id);
			if (existing.isPresent()) {
				Holder toSave = existing.get();
				toSave.setFirstName(holder.getFirstName());
				toSave.setLastName(holder.getLastName());
				toSave.setShares(holder.getShares());
				toSave.setStatus(holder.getStatus());
				toSave.setOrganization(holder.getOrganization());
				
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
			
			Optional<Holder> existing = repository.findById(id);
			if (existing.isPresent()) {
				Holder toDelete = existing.get();
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
