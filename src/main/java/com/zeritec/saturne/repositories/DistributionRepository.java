package com.zeritec.saturne.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.zeritec.saturne.models.Distribution;

public interface DistributionRepository extends CrudRepository<Distribution, Integer> {

	Optional<Distribution> findByCode(String code);
	
	Optional<Distribution> findByLabel(String label);
}
