package com.zeritec.saturne.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.zeritec.saturne.models.Holder;

public interface HolderRepository extends CrudRepository<Holder, Integer> {

	Optional<Holder> findByCode(String code);
}
