package com.zeritec.saturne.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zeritec.saturne.models.InvestmentRule;
import com.zeritec.saturne.models.InvestmentRuleType;
import com.zeritec.saturne.repositories.InvestmentRuleRepository;
import com.zeritec.saturne.utils.ExtractionSheetUtils;

@Service
public class InvestmentRuleService {

	@Autowired
	private InvestmentRuleRepository repository;
	
	public Iterable<InvestmentRule> getAll() {
		try {
			return repository.findAll();
		} catch (Exception e) {
			throw new RuntimeException("Erreur récupération liste " + e.getMessage());
		}
	}
	
	public Optional<InvestmentRule> getById(Integer id) {
		
		try {
			return repository.findById(id);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de recherche catégorie " + e.getMessage());
		}
	}
	
	public Optional<InvestmentRule> getByLabel(String code) {
		
		try {
			return repository.findByLabel(code);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de recherche  d action " + e.getMessage());
		}
	}
	
	public InvestmentRule create(InvestmentRule data) {
		try {
			return repository.save(data);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de création catégorie " + e.getMessage());
		}
	}
	
	public Optional<InvestmentRule> update(Integer id, InvestmentRule data) {
		try {
			
			Optional<InvestmentRule> existing = repository.findById(id);
			if (existing.isPresent()) {
				InvestmentRule toSave = existing.get();
				toSave.setLabel(data.getLabel());
				toSave.setValue(data.getValue());
				toSave.setPercent(data.getPercent());
				toSave.setOpc(data.getOpc());
				toSave.setInvestmentRuleType(data.getInvestmentRuleType());
				
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
			
			Optional<InvestmentRule> existing = repository.findById(id);
			if (existing.isPresent()) {
				InvestmentRule toDelete = existing.get();
				repository.delete(toDelete);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new RuntimeException("Erreur de suppression " + e.getMessage());
		}
	}
	
	public List<InvestmentRule> extractFromSheet(Workbook workbook, Iterable<InvestmentRuleType> types) {
		List<InvestmentRule> rules = new ArrayList<>();
		
		for(InvestmentRuleType type: types) {
			rules.addAll(this.extractLine(workbook, type.getLabel(), type));
		}
		
		return rules;
	}
	
	private List<InvestmentRule> extractLine(Workbook workbook, String compare, InvestmentRuleType type) {
		List<InvestmentRule> rules = new ArrayList<>();
		boolean stop = false;
		boolean write = false;
		
		for(int i=14 ; i < workbook.getSheetAt(0).getPhysicalNumberOfRows(); i++) {
			Row row = workbook.getSheetAt(0).getRow(i);
			
			if (row == null) {
				return rules;
			} else {
				String label = row.getCell(0).getStringCellValue();
				if (label.equals(compare)) {
					write = true;
				} else {
					if (write) {
						
						if (label.length() != 0 ) {
							InvestmentRule rule = new InvestmentRule();
							
							rule.setLabel(label);
							if (label.toLowerCase().contains("total")) {
								stop = true;
							} 
							
							if (row.getCell(1).getCellType() !=  CellType.BLANK) rule.setValue(ExtractionSheetUtils.extractNumber(row.getCell(1)));
							if (row.getCell(2).getCellType() !=  CellType.BLANK) rule.setPercent(ExtractionSheetUtils.extractNumber(row.getCell(2)));
							rule.setInvestmentRuleType(type);
							
							rules.add(rule);
						}
						
					}
				}
				
				if (stop) {
					return rules;
				}
			}
		}
		
		return rules;
	}
}
