package com.zeritec.saturne.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.zeritec.saturne.models.Role;
import com.zeritec.saturne.models.VisaApplication;

public interface VisaRepository extends CrudRepository<VisaApplication, Integer> {

	Optional<VisaApplication> findByLabel(String label);
}
