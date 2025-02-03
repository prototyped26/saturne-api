package com.zeritec.saturne.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.zeritec.saturne.models.Depositary;

public interface DepositaryRepository extends CrudRepository<Depositary, Integer> {

	Optional<Depositary> findByCode(String code);
}
