package com.zeritec.saturne.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.zeritec.saturne.models.Category;

public interface CategoryRepository extends CrudRepository<Category, Integer> {

	Optional<Category> findByCode(String code);
	
	Optional<Category> findByLabel(String label);
}
