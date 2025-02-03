package com.zeritec.saturne.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {
	public static Date asDate(String chain) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate d = LocalDate.parse("23/03/2024", format);
		
		return Date.from(d.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}
}
