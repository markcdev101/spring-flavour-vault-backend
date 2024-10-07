package com.flavourvault.flavour_vault_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.flavourvault.flavour_vault_backend.dto.RegisterUserDto;
import com.flavourvault.flavour_vault_backend.entities.Role;
import com.flavourvault.flavour_vault_backend.entities.User;
import com.flavourvault.flavour_vault_backend.model.RoleEnum;
import com.flavourvault.flavour_vault_backend.repository.RoleRepository;
import com.flavourvault.flavour_vault_backend.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;


	public List<User> allUsers() {
		List<User> users = new ArrayList<>();

		userRepository.findAll().forEach(users::add);

		return users;
	}

	public User createAdministrator(RegisterUserDto input) {
		Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.ADMIN);

		if (optionalRole.isEmpty()) {
			return null;
		}

		User user = new User();
		user.setFullName(input.getFullName());
		user.setEmail(input.getEmail());
		user.setPassword(passwordEncoder.encode(input.getPassword()));
		user.setRole(optionalRole.get());

		return userRepository.save(user);
	}
}
