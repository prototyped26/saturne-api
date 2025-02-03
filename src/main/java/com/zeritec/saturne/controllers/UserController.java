package com.zeritec.saturne.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zeritec.saturne.models.ChangePasswordRequest;
import com.zeritec.saturne.models.RequestResponse;
import com.zeritec.saturne.models.LoginRequest;
import com.zeritec.saturne.models.RegisterRequest;
import com.zeritec.saturne.models.Role;
import com.zeritec.saturne.models.User;
import com.zeritec.saturne.services.JwtService;
import com.zeritec.saturne.services.RoleService;
import com.zeritec.saturne.services.UserService;

@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostMapping("/register")
	public User register(@RequestBody RegisterRequest request) {
		Optional<Role> roleOpt = roleService.getById(request.getRoleId());
		if (roleOpt.isEmpty()) {
			throw new RequestRejectedException("Veuillez vérifier le role ! ");
		}
		
		User user = new User();
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setEmail(request.getEmail());
		user.setLogin(request.getLogin());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole(roleOpt.get());
		return userService.register(user);
	}
	
	@PostMapping("/login")
	public String authenticate(@RequestBody LoginRequest request) {
		
		try {
			Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
			if (authentication.isAuthenticated()) {
				//return jwtService.generateToken2(userService.loadUserByUsername(request.getLogin()));
				return jwtService.generateToken(authentication);
			} else {
				throw new UsernameNotFoundException("Invalid credential !");
			}
		} catch (Exception e) {
			throw new UsernameNotFoundException("Invalid credential !");
		}
		
	}
	
	@GetMapping("/users")
	public ResponseEntity<RequestResponse> getUsers() {
		RequestResponse response = new RequestResponse();
		
		try {
			response.setData(userService.getAll());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/users/{id}")
	public ResponseEntity<RequestResponse> getUser(@PathVariable("id") int id) {
		RequestResponse response = new RequestResponse();
		
		try {
			
			Optional<User> res = userService.getById(id);
			if (res.isPresent()) {
				response.setData(res.get());
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setMessage("Utilisateur non existant");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/users/{id}/password")
	public ResponseEntity<RequestResponse> changePassword(@PathVariable("id") int id ,@RequestBody ChangePasswordRequest req) {
		RequestResponse response = new RequestResponse();
		
		try {
			req.setNewPassword(passwordEncoder.encode(req.getNewPassword()));
			Optional<User> res = userService.updatePassword(id, req);
			if (res.isPresent()) {
				response.setMessage("changement de mot de passe fait !");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setMessage("Erreur de changement, veuillez vérifier les données.");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/user")
	public ResponseEntity<RequestResponse> currentUser(@CurrentSecurityContext(expression = "authentication") Authentication authentication) {
		RequestResponse response = new RequestResponse();
		
		try {
			Optional<User> optUser = userService.getByLogin(authentication.getName());
			if (optUser.isPresent()) {
				response.setData(optUser.get());
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@PostMapping("/users/{id}")
	public ResponseEntity<RequestResponse> update(@PathVariable("id") int id ,@RequestBody User user) {
		RequestResponse response = new RequestResponse();
		
		try {
			
			Optional<User> res = userService.update(id, user);
			if (res.isPresent()) {
				response.setData(res.get());
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setMessage("Erreur de traitement, veuillez vérifier les données.");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("/users/{id}")
	public ResponseEntity<RequestResponse> delete(@PathVariable("id") int id) {
		RequestResponse response = new RequestResponse();
		
		if (id == 1) {
			response.setMessage("Impossible de supprimé cet utilisateur.");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		
		try {
			
			boolean res = userService.delete(id);
			if (res) {
				response.setMessage("Action effectuée avec succès ! ");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setMessage("Erreur de suppression veuillez vérifier les données.");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
}
