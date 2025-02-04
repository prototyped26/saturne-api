package com.zeritec.saturne.models.request;

import com.zeritec.saturne.models.Week;
import com.zeritec.saturne.models.Year;

import lombok.Data;

@Data
public class RequestInformation {

	private Year year;
	
	private Iterable<Week> weeks;
	
	private Week currentWeek;
}
