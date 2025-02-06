package com.zeritec.saturne.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.zeritec.saturne.models.SgoEmployee;


public interface SgoEmployeeRepository extends CrudRepository<SgoEmployee, Integer> {

	Optional<SgoEmployee> findByLabel(String label);
}
