package com.zeritec.saturne.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zeritec.saturne.models.AssetLine;
import com.zeritec.saturne.models.AssetType;
import com.zeritec.saturne.repositories.AssetLineRepository;
import com.zeritec.saturne.utils.ExtractionSheetUtils;

@Service
public class AssetLineService {

	@Autowired
	private AssetLineRepository repository;
	
	public Iterable<AssetLine> getAll() {
		try {
			return repository.findAll();
		} catch (Exception e) {
			throw new RuntimeException("Erreur récupération liste " + e.getMessage());
		}
	}
	
	public Optional<AssetLine> getById(Integer id) {
		
		try {
			return repository.findById(id);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de recherche catégorie " + e.getMessage());
		}
	}
	
	public Optional<AssetLine> getByLabel(String code) {
		
		try {
			return repository.findByLabel(code);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de recherche  d action " + e.getMessage());
		}
	}
	
	public AssetLine create(AssetLine data) {
		try {
			return repository.save(data);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de création catégorie " + e.getMessage());
		}
	}
	
	public Optional<AssetLine> update(Integer id, AssetLine data) {
		try {
			
			Optional<AssetLine> existing = repository.findById(id);
			if (existing.isPresent()) {
				AssetLine toSave = existing.get();
				toSave.setLabel(data.getLabel());
				toSave.setValue(data.getValue());
				toSave.setPercent(data.getPercent());
				toSave.setOpc(data.getOpc());
				toSave.setAssetType(data.getAssetType());
				
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
			
			Optional<AssetLine> existing = repository.findById(id);
			if (existing.isPresent()) {
				AssetLine toDelete = existing.get();
				repository.delete(toDelete);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new RuntimeException("Erreur de suppression " + e.getMessage());
		}
	}
	
	public List<AssetLine> extractFromSheet(Workbook workbook, Iterable<AssetType> types) {
		 List<AssetLine> lines = new ArrayList<>();
		
		 for(AssetType type: types) {
			 lines.addAll(this.extractLine(workbook, type.getLabel(), type));
		 }
		 
		 return lines;
	}
	
	private List<AssetLine> extractLine(Workbook workbook, String compare, AssetType type) {
	 	List<AssetLine> lines = new ArrayList<>();
	 	boolean stop = false;
	 	//System.out.println("--- C : " + compare);
		 
		for(int i=14 ; i < workbook.getSheetAt(0).getPhysicalNumberOfRows(); i++) {
			Row row = workbook.getSheetAt(0).getRow(i);
			
			if (row != null) {
				if (row != null && !row.equals(null)) {
					String label = row.getCell(0).getStringCellValue();
					if (label.equals(compare)) {
						// write = true;
						AssetLine line = new AssetLine();
						
						line.setLabel(label);
						line.setValue(ExtractionSheetUtils.extractNumber(row.getCell(3)));
						line.setPercent(ExtractionSheetUtils.extractNumber(row.getCell(4)));
						line.setAssetType(type);
						
						lines.add(line);
						stop = true;
					}
				}
			}
			
			if (stop) return lines;
		}
		 
		return lines;
	}
}
