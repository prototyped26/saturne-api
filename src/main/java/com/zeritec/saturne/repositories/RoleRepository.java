package com.zeritec.saturne.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.zeritec.saturne.models.Role;

public interface RoleRepository extends CrudRepository<Role, Integer> {

	Optional<Role> findByCode(String code);
}
