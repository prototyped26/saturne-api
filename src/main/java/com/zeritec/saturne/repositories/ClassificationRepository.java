package com.zeritec.saturne.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.zeritec.saturne.models.Classification;

public interface ClassificationRepository extends CrudRepository<Classification, Integer> {

	Optional<Classification> findByCode(String code);
}
