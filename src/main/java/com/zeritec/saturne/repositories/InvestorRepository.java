package com.zeritec.saturne.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.zeritec.saturne.models.Investor;


public interface InvestorRepository extends CrudRepository<Investor, Integer> {

	Optional<Investor> findByLabel(String label);
	
	Iterable<Investor> findByUnit(String unit);
}
