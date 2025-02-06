package com.zeritec.saturne.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.zeritec.saturne.models.InvestmentRule;

public interface InvestmentRuleRepository extends CrudRepository<InvestmentRule, Integer> {

	Optional<InvestmentRule> findByLabel(String code);
}
