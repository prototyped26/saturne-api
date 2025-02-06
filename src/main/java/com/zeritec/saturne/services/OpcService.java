package com.zeritec.saturne.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zeritec.saturne.models.Opc;
import com.zeritec.saturne.repositories.OpcRepository;

@Service
public class OpcService {

	@Autowired
	private OpcRepository repository;
	
	public Iterable<Opc> getAll() {
		try {
			return repository.findAll();
		} catch (Exception e) {
			throw new RuntimeException("Erreur récupération liste " + e.getMessage());
		}
	}
	
	public Optional<Opc> getById(Integer id) {
		
		try {
			return repository.findById(id);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de recherche OPCV " + e.getMessage());
		}
	}
	
	public Iterable<Opc> getByWeek(int week) {
		
		try {
			return repository.findByWeekId(week);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de recherche  d OPCV " + e.getMessage());
		}
	}
	
	public Opc create(Opc data) {
		try {
			return repository.save(data);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de création catégorie " + e.getMessage());
		}
	}
	
	public Optional<Opc> update(Integer id, Opc data) {
		try {
			
			Optional<Opc> existing = repository.findById(id);
			if (existing.isPresent()) {
				Opc toSave = existing.get();
				toSave.setCreatedAt(data.getCreatedAt());
				toSave.setEstimatedAt(data.getEstimatedAt());
				toSave.setFund(data.getFund());
				toSave.setWeek(data.getWeek());
				
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
			
			Optional<Opc> existing = repository.findById(id);
			if (existing.isPresent()) {
				Opc toDelete = existing.get();
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
