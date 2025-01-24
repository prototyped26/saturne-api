package com.zeritec.saturne.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.zeritec.saturne.models.ChangePasswordRequest;
import com.zeritec.saturne.models.Role;
import com.zeritec.saturne.models.User;
import com.zeritec.saturne.repositories.UserRepositoty;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepositoty repository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<User> userOpt = repository.findByEmail(username);
		if (userOpt.isPresent()) {
			User user = userOpt.get();
			return org.springframework.security.core.userdetails.User.builder()
					.username(user.getLogin())
					.password(user.getPassword())
					.roles(user.getRole().getCode())
					.build();
		} else {
			throw new UsernameNotFoundException(username);
		}
	}
	
	public Iterable<User> getAll() {
		try {
			return repository.findAll();
		} catch (Exception e) {
			throw new RuntimeException("Erreur lors de la laecture de la liste  d users " + e.getMessage());
		}
	}
	
	public Iterable<User> getByRole(Role role) {
		try {
			return repository.findByRoleId(role.getId());
		} catch (Exception e) {
			throw new RuntimeException("Erreur lors de la laecture de la liste  d users " + e.getMessage());
		}
	}
	 
	public User register(User user) {
		try {
			return repository.save(user);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de création d user " + e.getMessage());
		}
	}
	
	public Optional<User> getById(Integer id) {
		
		try {
			return repository.findById(id);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de recherche  d user " + e.getMessage());
		}
	}
	
	public Optional<User> getByLogin(String login) {
		
		try {
			return repository.findByLogin(login);
		} catch (Exception e) {
			throw new RuntimeException("Erreur de recherche  d user " + e.getMessage());
		}
	}
	
	public Optional<User> updatePassword(Integer id, ChangePasswordRequest req) {
		try {
			
			Optional<User> existing = repository.findById(id);
			if (existing.isPresent()) {
				User toSave = existing.get();
				
				toSave.setPassword(req.getNewPassword());
				
				return Optional.of(repository.save(toSave));
			} else {
				return Optional.empty();
			}
			
		} catch (Exception e) {
			throw new RuntimeException("Erreur de mise à jour" + e.getMessage());
		}
	}
	
	public Optional<User> update(Integer id, User user) {
		try {
			
			Optional<User> existing = repository.findById(id);
			if (existing.isPresent()) {
				User toSave = existing.get();
				toSave.setFirstName(user.getFirstName());
				toSave.setLastName(user.getLastName());
				
				return Optional.of(repository.save(toSave));
			} else {
				return Optional.empty();
			}
			
		} catch (Exception e) {
			throw new RuntimeException("Erreur de mise à jour" + e.getMessage());
		}
	}
	
	public boolean delete(Integer id) {
		try {
			
			Optional<User> existing = repository.findById(id);
			if (existing.isPresent()) {
				User toDelete = existing.get();
				repository.delete(toDelete);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new RuntimeException("Erreur de suppression " + e.getMessage());
		}
	}
}
