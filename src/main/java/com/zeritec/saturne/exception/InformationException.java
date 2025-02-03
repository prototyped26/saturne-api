package com.zeritec.saturne.exception;

import java.sql.SQLException;

public class InformationException {

	public static String getMessageIn(Exception e) {
		System.out.println(e.getClass().getSimpleName());
		if (e instanceof SQLException) {
			return "C'est bien une erreur sql";
		} else {
			return e.getMessage();
		}
	}
	
}
