package com.flavourvault.flavour_vault_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.flavourvault.flavour_vault_backend.dto.RegisterUserDto;
import com.flavourvault.flavour_vault_backend.entities.Profile;
import com.flavourvault.flavour_vault_backend.entities.Role;
import com.flavourvault.flavour_vault_backend.entities.User;
import com.flavourvault.flavour_vault_backend.exceptions.DuplicateUsernameException;
import com.flavourvault.flavour_vault_backend.exceptions.ProfileNotFoundException;
import com.flavourvault.flavour_vault_backend.model.RoleEnum;
import com.flavourvault.flavour_vault_backend.repository.ProfileRepository;
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
	private ProfileRepository profileRepository;

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
		
		 // Check if a profile exists with the provided email
        Optional<Profile> optionalProfile = profileRepository.findByEmail(input.getEmail());
        
        if (optionalProfile.isEmpty()) {
            // Handle the case where no profile is found (optional)
            System.err.println("No profile found for email: " + input.getEmail());
            throw new ProfileNotFoundException("Profile not found for email: " + input.getEmail());
        }
        
        // Check if username already exists
        if (userRepository.findByUsername(input.getUsername()).isPresent()) {
            throw new DuplicateUsernameException("Username " + input.getUsername());
        }

		User user = new User();
		user.setFullName(input.getFullName());
		user.setUsername(input.getUsername());
		user.setPassword(passwordEncoder.encode(input.getPassword()));
		user.setRole(optionalRole.get());
		user.setProfile(optionalProfile.get()); // Assigning the found profile

		return userRepository.save(user);
	}
}
