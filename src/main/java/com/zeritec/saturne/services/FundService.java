package com.zeritec.saturne.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zeritec.saturne.models.Fund;
import com.zeritec.saturne.models.request.RequestFund;
import com.zeritec.saturne.repositories.FundRepository;
import com.zeritec.saturne.utils.DateUtils;

@Service
public class FundService {

	@Autowired
	private FundRepository repository;
	
	public Iterable<Fund> getAll() {
		try {
			return repository.findAll();
		} catch (Exception e) {
			throw new RuntimeException("Erreur récupération liste " + e.getMessage());
		}
	}
	
	public Optional<Fund> getById(Integer id) {
		
		try {
			return repository.findById(id);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de recherche catégorie " + e.getMessage());
		}
	}
	
	public Optional<Fund> getByApprovalNumber(String number) {
		
		try {
			return repository.findByApprovalNumber(number);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de recherche  d action " + e.getMessage());
		}
	}
	
	public Optional<Fund> getByLabel(String code) {
		
		try {
			return repository.findByLabel(code);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de recherche  d action " + e.getMessage());
		}
	}
	
	public Fund create(Fund fund) {
		try {
			return repository.save(fund);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de création catégorie " + e.getMessage());
		}
	}
	
	public Optional<Fund> update(Integer id, Fund fund) {
		try {
			
			Optional<Fund> existing = repository.findById(id);
			if (existing.isPresent()) {
				Fund toSave = existing.get();
				toSave.setOther(fund.getOther());
				toSave.setLabel(fund.getLabel());
				toSave.setApprovalNumber(fund.getApprovalNumber());
				toSave.setApprovalDate(fund.getApprovalDate());
				toSave.setDistributionNetwork(fund.getDistributionNetwork());
				toSave.setTypeOpc(fund.getTypeOpc());
				toSave.setDepositary(fund.getDepositary());
				toSave.setClassification(fund.getClassification());
				toSave.setDistribution(fund.getDistribution());
				toSave.setIntermediary(fund.getIntermediary());
				toSave.setAuditor(fund.getAuditor());
				
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
			
			Optional<Fund> existing = repository.findById(id);
			if (existing.isPresent()) {
				Fund toDelete = existing.get();
				repository.delete(toDelete);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new RuntimeException("Erreur de suppression " + e.getMessage());
		}
	}
	
	public Fund convertRequest(RequestFund req) {
		Fund fund = new Fund();
		fund.setOther(req.getOther());
		fund.setLabel(req.getLabel());
		fund.setApprovalDate(DateUtils.asDate(req.getApprovalDate()));
		fund.setApprovalNumber(req.getApprovalNumber());
		fund.setDistributionNetwork(req.getDistributionNetwork());
		
		return fund;
	}
	
	public String normalizeApprovalNumber(String str) {
		String res =  str.replace("N", "");
		res = res.replace("°", "");
		res = res.replaceAll("\\s", "");
		
		return res;
	}
}
