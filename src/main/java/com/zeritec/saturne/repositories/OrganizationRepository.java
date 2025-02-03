package com.zeritec.saturne.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.zeritec.saturne.models.Organization;

public interface OrganizationRepository extends CrudRepository<Organization, Integer> {

	Optional<Organization> findByLabel(String label);
}
