package com.zeritec.saturne.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.zeritec.saturne.models.AssetType;


public interface AssetTypeRepository extends CrudRepository<AssetType, Integer> {

	Optional<AssetType> findByCode(String code);
}
