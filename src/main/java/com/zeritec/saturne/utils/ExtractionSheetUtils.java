package com.zeritec.saturne.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

public class ExtractionSheetUtils {

	public static String extractString(Cell cell) {
		String res = null;
		try {
			switch (cell.getCellType()) {
				case NUMERIC: 
					res = "" + cell.getNumericCellValue();
				case FORMULA: {
					if (cell.getCachedFormulaResultType() == CellType.STRING) {
						res = cell.getStringCellValue();
					} else {
						res = "" + cell.getNumericCellValue();
					}
				}
				default:
					res = cell.getStringCellValue();
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.print("EXTRACT STRING CELL SHEET ERROR => " + cell.toString() + " : " + e.getMessage());
		}
		
		return res;
	}
	
	public static Double extractNumber(Cell cell) {
		Double res = null;
		if (cell != null) {
			try {
				switch (cell.getCellType()) {
					case NUMERIC: 
						res = cell.getNumericCellValue();
					case FORMULA: {
						if (cell.getCachedFormulaResultType() == CellType.STRING) {
							String str = cell.getStringCellValue();
							if (str.length() > 0) res = Double.parseDouble(str);
						} else {
							res = cell.getNumericCellValue();
						}
					}
					default:
						res = Double.parseDouble(cell.getStringCellValue());
				}
			} catch (Exception e) {
				// TODO: handle exception
				//System.out.println("EXTRACT STRING CELL SHEET ERROR => " + cell.toString() + " TYPE : " + cell.getCellType().toString() + " : " + e.getMessage());
			}
		}
		return res;
	}
}
