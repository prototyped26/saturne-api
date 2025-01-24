package com.zeritec.saturne.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.zeritec.saturne.models.User;

public interface UserRepositoty extends CrudRepository<User, Integer> {
	Optional<User> findByLogin(String login);
	
	Optional<User> findByEmail(String email);
	
	Iterable<User> findByRoleId(int id);
}
