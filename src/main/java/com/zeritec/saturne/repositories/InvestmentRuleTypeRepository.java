package com.zeritec.saturne.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.zeritec.saturne.models.InvestmentRuleType;

public interface InvestmentRuleTypeRepository extends CrudRepository<InvestmentRuleType, Integer> {

	Optional<InvestmentRuleType> findByCode(String code);
}
