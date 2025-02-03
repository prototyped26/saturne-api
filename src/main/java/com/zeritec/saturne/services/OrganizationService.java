package com.zeritec.saturne.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zeritec.saturne.models.Organization;
import com.zeritec.saturne.repositories.OrganizationRepository;

@Service
public class OrganizationService {

	@Autowired
	private OrganizationRepository repository;
	
	public Iterable<Organization> getAll() {
		try {
			return repository.findAll();
		} catch (Exception e) {
			throw new RuntimeException("Erreur récupération liste " + e.getMessage());
		}
	}
	
	public Optional<Organization> getById(Integer id) {
		
		try {
			return repository.findById(id);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de recherche  " + e.getMessage());
		}
	}
	
	public Optional<Organization> getByLabel(String code) {
		
		try {
			return repository.findByLabel(code);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de recherche " + e.getMessage());
		}
	}
	
	public Organization create(Organization organization) {
		try {
			return repository.save(organization);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de création  " + e.getMessage());
		}
	}
	
	public Optional<Organization> update(Integer id, Organization org) {
		try {
			
			Optional<Organization> existing = repository.findById(id);
			if (existing.isPresent()) {
				Organization toSave = existing.get();
				toSave.setLabel(org.getLabel());
				toSave.setHeader(org.getHeader());
				toSave.setCapital(org.getCapital());
				toSave.setStatus(org.getStatus());
				
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
			
			Optional<Organization> existing = repository.findById(id);
			if (existing.isPresent()) {
				Organization toDelete = existing.get();
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
