package com.zeritec.saturne.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zeritec.saturne.models.Role;
import com.zeritec.saturne.repositories.RoleRepository;

@Service
public class RoleService {

	@Autowired
	private RoleRepository repository;
	
	public Iterable<Role> getAll() {
		try {
			return repository.findAll();
		} catch (Exception e) {
			throw new RuntimeException("Erreur lors de la laecture de la liste  d roles " + e.getMessage());
		}
	}
	
	public Optional<Role> getById(Integer id) {
		
		try {
			return repository.findById(id);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de recherche  d action " + e.getMessage());
		}
	}
	
	public Optional<Role> getByCode(String code) {
		
		try {
			return repository.findByCode(code);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de recherche  d action " + e.getMessage());
		}
	}
	
	public Role create(Role role) {
		try {
			return repository.save(role);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de création d role " + e.getMessage());
		}
	}
	
	public Optional<Role> update(Integer id, Role role) {
		try {
			
			Optional<Role> existing = repository.findById(id);
			if (existing.isPresent()) {
				Role toSave = existing.get();
				toSave.setCode(role.getCode());
				toSave.setLabel(role.getLabel());
				
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
			
			Optional<Role> existing = repository.findById(id);
			if (existing.isPresent()) {
				Role toDelete = existing.get();
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
