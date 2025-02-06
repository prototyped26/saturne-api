package com.zeritec.saturne.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.zeritec.saturne.models.AssetLine;


public interface AssetLineRepository extends CrudRepository<AssetLine, Integer> {

	Optional<AssetLine> findByLabel(String label);
}
