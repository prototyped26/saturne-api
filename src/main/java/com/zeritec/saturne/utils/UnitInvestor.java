package com.zeritec.saturne.utils;

public enum UnitInvestor {

	PART_ACTIONS("Nombre total de parts/actions"), INVESTOR_OPCVM("Nombre total d'investisseurs dans l'OPCVM");
	
	private final String value;
	
	private UnitInvestor(String str) {
		this.value = str;
	}
	
	public String getValue() {
		return this.value;
	}
	
}
