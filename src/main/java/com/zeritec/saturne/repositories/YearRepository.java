package com.zeritec.saturne.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.zeritec.saturne.models.Year;


public interface YearRepository extends CrudRepository<Year, Integer> {

	Optional<Year> findByCode(String code);
	
	Iterable<Year> findByActiveTrue();
}
