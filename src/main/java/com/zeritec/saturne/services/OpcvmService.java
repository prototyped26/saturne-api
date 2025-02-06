package com.zeritec.saturne.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zeritec.saturne.models.Opcvm;
import com.zeritec.saturne.models.OpcvmType;
import com.zeritec.saturne.repositories.OpcvmRepository;
import com.zeritec.saturne.utils.ExtractionSheetUtils;

@Service
public class OpcvmService {

	@Autowired
	private OpcvmRepository repository;
	
	public Iterable<Opcvm> getAll() {
		try {
			return repository.findAll();
		} catch (Exception e) {
			throw new RuntimeException("Erreur récupération liste " + e.getMessage());
		}
	}
	
	public Optional<Opcvm> getById(Integer id) {
		
		try {
			return repository.findById(id);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de recherche OPCVM " + e.getMessage());
		}
	}
	
	public Optional<Opcvm> getByLabel(String code) {
		
		try {
			return repository.findByLabel(code);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de recherche  d OPCVM " + e.getMessage());
		}
	}
	
	public Opcvm create(Opcvm data) {
		try {
			return repository.save(data);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de création catégorie " + e.getMessage());
		}
	}
	
	public Optional<Opcvm> update(Integer id, Opcvm data) {
		try {
			
			Optional<Opcvm> existing = repository.findById(id);
			if (existing.isPresent()) {
				Opcvm toSave = existing.get();
				toSave.setLabel(data.getLabel());
				toSave.setValue(data.getValue());
				toSave.setPercent(data.getPercent());
				toSave.setOpc(data.getOpc());
				toSave.setOpcvmType(data.getOpcvmType());
				
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
			
			Optional<Opcvm> existing = repository.findById(id);
			if (existing.isPresent()) {
				Opcvm toDelete = existing.get();
				repository.delete(toDelete);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new RuntimeException("Erreur de suppression " + e.getMessage());
		}
	}
	
	public List<Opcvm> extractOpcvmFromSheet(Workbook workbook, Iterable<OpcvmType> types) {
		List<Opcvm> opcvms = new ArrayList<>();
		
		types.forEach(type -> {
			if (type.getSubType().size() == 0) {
				// pas de sous éléments
				String refLabel = type.getLabel();
				opcvms.addAll(this.extractLine(workbook, refLabel, type));
			} else {
				type.getSubType().forEach(subType -> {
					String refLabel = subType.getLabel();
					opcvms.addAll(this.extractLine(workbook, refLabel, subType));
				});
			}
			
		});
		
		return opcvms;
	}
	
	private List<Opcvm> extractLine(Workbook workbook, String compare, OpcvmType type) {
		List<Opcvm> opcvms = new ArrayList<>();
		boolean stop = false;
		boolean write = false;
		for(int i=14 ; i < workbook.getSheetAt(0).getPhysicalNumberOfRows(); i++) {
			Row row = workbook.getSheetAt(0).getRow(i);
			
			if (row == null) {
				return opcvms;
			} else {
				String label = row.getCell(0).getStringCellValue();
				if (label.equals(compare)) {
					write = true;
				} else {
					if (write) {
						
						if (label.length() != 0 && row.getCell(3).getCellType() != CellType.BLANK) {
							Opcvm opcvm = new Opcvm();
							
							opcvm.setLabel(label);
							if (label.toLowerCase().contains("total")) {
								stop = true;
							} 
							
							opcvm.setNumber(ExtractionSheetUtils.extractNumber(row.getCell(1)));
							opcvm.setCours(ExtractionSheetUtils.extractNumber(row.getCell(2)));
							opcvm.setValue(ExtractionSheetUtils.extractNumber(row.getCell(3)));
							opcvm.setPercent(ExtractionSheetUtils.extractNumber(row.getCell(4)));
							opcvm.setOpcvmType(type);
							
							opcvms.add(opcvm);
						}
						
					}
				}
				
				if (stop) {
					return opcvms;
				}
			}
		}
		return opcvms;
	}
}
