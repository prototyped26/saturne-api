package com.zeritec.saturne.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.zeritec.saturne.models.TypeOpc;

public interface TypeOPCRepository extends CrudRepository<TypeOpc, Integer> {

	Optional<TypeOpc> findByCode(String code);
}
