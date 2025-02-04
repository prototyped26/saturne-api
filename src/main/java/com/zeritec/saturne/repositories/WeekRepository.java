package com.zeritec.saturne.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.zeritec.saturne.models.Week;

public interface WeekRepository extends CrudRepository<Week, Integer> {
	
	Optional<Week> findByLabel(String label);
	
	Optional<Week> findByNumber(int nubr);
	
	Iterable<Week> findByYearId(int id);
}
