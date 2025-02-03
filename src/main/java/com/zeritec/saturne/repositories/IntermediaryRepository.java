package com.zeritec.saturne.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.zeritec.saturne.models.Intermediary;

public interface IntermediaryRepository extends CrudRepository<Intermediary, Integer> {

	Optional<Intermediary> findByLabel(String label);
	
	Optional<Intermediary> findByApprovalNumber(String approvalNumber);
	
	Optional<Intermediary> findByLeaderName(String leaderName);
}
