package com.zeritec.saturne.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.zeritec.saturne.models.OpcvmType;

public interface OpcvmTypeRepository extends CrudRepository<OpcvmType, Integer> {
	
	Iterable<OpcvmType> findByParentIdNull();

	Optional<OpcvmType> findByCode(String code);
}
