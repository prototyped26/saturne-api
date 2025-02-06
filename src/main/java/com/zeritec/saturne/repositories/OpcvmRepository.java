package com.zeritec.saturne.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.zeritec.saturne.models.Opcvm;


public interface OpcvmRepository extends CrudRepository<Opcvm, Integer> {

	Optional<Opcvm> findByLabel(String label);
}
