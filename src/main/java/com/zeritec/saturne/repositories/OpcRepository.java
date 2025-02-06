package com.zeritec.saturne.repositories;


import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.zeritec.saturne.models.Opc;


public interface OpcRepository extends CrudRepository<Opc, Integer> {

	Iterable<Opc> findByFundId(int id);
	
	Iterable<Opc> findByWeekId(int id);
	
	Optional<Opc> findByFundIdAndWeekId(int fundId, int weekId);
}
