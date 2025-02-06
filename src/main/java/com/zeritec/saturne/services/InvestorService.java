package com.zeritec.saturne.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zeritec.saturne.models.Investor;
import com.zeritec.saturne.repositories.InvestorRepository;
import com.zeritec.saturne.utils.ExtractionSheetUtils;
import com.zeritec.saturne.utils.UnitInvestor;

@Service
public class InvestorService {

	@Autowired
	private InvestorRepository repository;
	
	public Iterable<Investor> getAll() {
		try {
			return repository.findAll();
		} catch (Exception e) {
			throw new RuntimeException("Erreur récupération liste " + e.getMessage());
		}
	}
	
	public Optional<Investor> getById(Integer id) {
		
		try {
			return repository.findById(id);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de recherche investisseur " + e.getMessage());
		}
	}
	
	public Optional<Investor> getByLabel(String label) {
		
		try {
			return repository.findByLabel(label);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de recherche  d investisseur " + e.getMessage());
		}
	}
	
	public Investor create(Investor data) {
		try {
			return repository.save(data);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de création l'investisseur " + e.getMessage());
		}
	}
	
	public Optional<Investor> update(Integer id, Investor data) {
		try {
			
			Optional<Investor> existing = repository.findById(id);
			if (existing.isPresent()) {
				Investor toSave = existing.get();
				toSave.setLabel(data.getLabel());
				toSave.setValue(data.getValue());
				toSave.setPercent(data.getPercent());
				toSave.setUnit(data.getUnit());
				toSave.setOpc(data.getOpc());
				
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
			
			Optional<Investor> existing = repository.findById(id);
			if (existing.isPresent()) {
				Investor toDelete = existing.get();
				repository.delete(toDelete);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new RuntimeException("Erreur de suppression " + e.getMessage());
		}
	}

	public List<Investor> extractFromSheet(Workbook workbook) {
		List<Investor> investors = new ArrayList<>();
		
		// Récupération des parts sgo premier tableau
		Integer indexParts = null;
		Integer indexInvestisseurs = null;
		for(int i=14 ; i < workbook.getSheetAt(0).getPhysicalNumberOfRows(); i++) {
			Row row = workbook.getSheetAt(0).getRow(i);
			
			if (row != null) {
				if (row != null && !row.equals(null)) {
					String label = row.getCell(0).getStringCellValue();
					if (label.contains(UnitInvestor.PART_ACTIONS.getValue())) {
						indexParts = i;
					}
					
					if (label.contains(UnitInvestor.INVESTOR_OPCVM.getValue())) {
						indexInvestisseurs = i;
					}
				}
			}
		}
		
		if (!indexParts.equals(null)) {
			for(int i=indexParts ; i < (indexParts + 4); i++) {
				Row row = workbook.getSheetAt(0).getRow(i);
				
				if (row != null) {
					if (row != null && !row.equals(null)) {
						String label = ExtractionSheetUtils.extractString(row.getCell(0));
						
						if (label.toLowerCase().contains("assimilé")) {
							System.out.println("Chaine assimilée trouvée ");
							break;
						}
						
						Investor investor = new Investor();
						investor.setLabel(label);
						
						if (row.getCell(1).getCellType() == CellType.BLANK )  investor.setValue(ExtractionSheetUtils.extractNumber(row.getCell(3)));
						else  investor.setValue(ExtractionSheetUtils.extractNumber(row.getCell(1)));
						
						if (row.getCell(2).getCellType() == CellType.BLANK )  investor.setPercent(ExtractionSheetUtils.extractNumber(row.getCell(4)));
						else  investor.setPercent(ExtractionSheetUtils.extractNumber(row.getCell(2)));
						
						investor.setUnit(UnitInvestor.PART_ACTIONS.getValue());
						
						investors.add(investor);
					}
				}
			}
		}
		
		if (!indexInvestisseurs.equals(null)) {
			for(int i = (indexInvestisseurs + 3) ; i < workbook.getSheetAt(0).getPhysicalNumberOfRows(); i++) {
				Row row = workbook.getSheetAt(0).getRow(i);
				if (row != null) {
					if (row != null && !row.equals(null)) {
						if (row.getCell(0).getCellType() != CellType.BLANK) {
							
							String label = ExtractionSheetUtils.extractString(row.getCell(0));
							
							if (label.toLowerCase().contains("assimilé")) {
								System.out.println("Chaine assimilée trouvée ");
								break;
							}
							
							Investor investor = new Investor();
							investor.setLabel(label);
							investor.setValue(ExtractionSheetUtils.extractNumber(row.getCell(1)));
							investor.setPercent(ExtractionSheetUtils.extractNumber(row.getCell(2)));
							
							if (row.getCell(3).getCellType() == CellType.BLANK ) { 
								investor.setQualified(false);
							} else {
								if (row.getCell(3).getStringCellValue().toUpperCase().contains("OUI")) {
									investor.setQualified(true);
								} else {
									investor.setQualified(false);
								}
							}
							
							investor.setUnit(UnitInvestor.INVESTOR_OPCVM.getValue());
							
							investors.add(investor);
						}
					}
				}
			}
		}
		
		return investors;
	}
	
}
