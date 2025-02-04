package com.zeritec.saturne.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.zeritec.saturne.models.Fund;

public interface FundRepository extends CrudRepository<Fund, Integer> {

	Optional<Fund> findByApprovalNumber(String number);
	
	Optional<Fund> findByLabel(String label);
}
