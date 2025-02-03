package com.zeritec.saturne.services;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zeritec.saturne.models.Intermediary;
import com.zeritec.saturne.models.request.RequestIntermediary;
import com.zeritec.saturne.repositories.IntermediaryRepository;
import com.zeritec.saturne.utils.DateUtils;

@Service
public class IntermediaryService {

	@Autowired
	private IntermediaryRepository repository;
	
	public Iterable<Intermediary> getAll() {
		try {
			return repository.findAll();
		} catch (Exception e) {
			throw new RuntimeException("Erreur sélection la liste  des intermediaires " + e.getMessage());
		}
	}
	
	public Optional<Intermediary> getById(Integer id) {
		
		try {
			return repository.findById(id);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de recherche d un intermédiaire " + e.getMessage());
		}
	}
	
	public Optional<Intermediary> getByLabel(String label) {
		
		try {
			return repository.findByLabel(label);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de recherche d un intermédiaire " + e.getMessage());
		}
	}
	
	public Optional<Intermediary> getByApprovalNumber(String number) {
		
		try {
			return repository.findByApprovalNumber(number);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de recherche  d un intermediaire " + e.getMessage());
		}
	}
	
	public Intermediary create(Intermediary inter) {
		try {
			return repository.save(inter);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de création d role " + e.getMessage());
		}
	}
	
	public Optional<Intermediary> update(Integer id, Intermediary inter) {
		try {
			Optional<Intermediary> existing = repository.findById(id);
			if (existing.isPresent()) {
				Intermediary toSave = existing.get();
				toSave.setLabel(inter.getLabel());
				toSave.setHead(inter.getHead());
				toSave.setApprovalNumber(inter.getApprovalNumber());
				toSave.setApprovalDate(inter.getApprovalDate());
				toSave.setLeaderName(inter.getLeaderName());
				toSave.setApprovalNumberTwo(inter.getApprovalNumberTwo());
				toSave.setApprovalDateTwo(inter.getApprovalDateTwo());
				toSave.setAdress(inter.getAdress());
				toSave.setContacts(inter.getContacts());
				toSave.setCategory(inter.getCategory());
				if (inter.getOrganization() != null) toSave.setOrganization(inter.getOrganization());
				
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
			
			Optional<Intermediary> existing = repository.findById(id);
			if (existing.isPresent()) {
				Intermediary toDelete = existing.get();
				repository.delete(toDelete);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new RuntimeException("Erreur de suppression " + e.getMessage());
		}
	}
	
	public Intermediary convertRequest(RequestIntermediary req) {
		Intermediary inter = new Intermediary();
		
		inter.setLabel(req.getLabel());
		inter.setHead(req.getHead());
		inter.setApprovalNumber(req.getApprovalNumber());
		inter.setApprovalDate(DateUtils.asDate(req.getApprovalDate()));
		inter.setLeaderName(req.getLeaderName());
		inter.setLeaderStatus(req.getLeaderStatus());
		inter.setApprovalNumberTwo(req.getApprovalNumberTwo());
		inter.setApprovalDateTwo(DateUtils.asDate(req.getApprovalDateTwo()));
		inter.setAdress(req.getAdress());
		inter.setContacts(req.getContacts());
		
		return inter;
	}

}
