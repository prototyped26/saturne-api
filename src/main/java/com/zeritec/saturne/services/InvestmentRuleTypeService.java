package com.zeritec.saturne.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zeritec.saturne.models.InvestmentRuleType;
import com.zeritec.saturne.repositories.InvestmentRuleTypeRepository;

@Service
public class InvestmentRuleTypeService {

	@Autowired
	private InvestmentRuleTypeRepository repository;
	
	public Iterable<InvestmentRuleType> getAll() {
		try {
			return repository.findAll();
		} catch (Exception e) {
			throw new RuntimeException("Erreur récupération liste " + e.getMessage());
		}
	}
	
	public Optional<InvestmentRuleType> getById(Integer id) {
		
		try {
			return repository.findById(id);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de recherche catégorie " + e.getMessage());
		}
	}
	
	public Optional<InvestmentRuleType> getByCode(String code) {
		
		try {
			return repository.findByCode(code);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de recherche  d action " + e.getMessage());
		}
	}
	
	public InvestmentRuleType create(InvestmentRuleType data) {
		try {
			return repository.save(data);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de création catégorie " + e.getMessage());
		}
	}
	
	public Optional<InvestmentRuleType> update(Integer id, InvestmentRuleType data) {
		try {
			
			Optional<InvestmentRuleType> existing = repository.findById(id);
			if (existing.isPresent()) {
				InvestmentRuleType toSave = existing.get();
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
			
			Optional<InvestmentRuleType> existing = repository.findById(id);
			if (existing.isPresent()) {
				InvestmentRuleType toDelete = existing.get();
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
