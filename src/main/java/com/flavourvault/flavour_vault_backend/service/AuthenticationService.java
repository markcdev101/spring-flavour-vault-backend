package com.flavourvault.flavour_vault_backend.service;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.flavourvault.flavour_vault_backend.dto.LoginUserDto;
import com.flavourvault.flavour_vault_backend.dto.RegisterUserDto;
import com.flavourvault.flavour_vault_backend.entities.Profile;
import com.flavourvault.flavour_vault_backend.entities.Role;
import com.flavourvault.flavour_vault_backend.entities.User;
import com.flavourvault.flavour_vault_backend.model.RoleEnum;
import com.flavourvault.flavour_vault_backend.repository.ProfileRepository;
import com.flavourvault.flavour_vault_backend.repository.RoleRepository;
import com.flavourvault.flavour_vault_backend.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthenticationService {
	
	@Autowired
    private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
    
	@Autowired
    private PasswordEncoder passwordEncoder;
    
	@Autowired
    private AuthenticationManager authenticationManager;
	
	@Autowired
    private ProfileRepository profileRepository;


    public User signup(RegisterUserDto input) {
    	Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER);
        
        if (optionalRole.isEmpty()) {
            return null;
        }
    	
        
		Profile profile = new Profile();
		profile.setEmail(input.getEmail());
		profileRepository.save(profile);
    	
    	User user = new User();
        user.setFullName(input.getFullName());
        user.setUsername(input.getUsername());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setRole(optionalRole.get());
        user.setProfile(profile);
        
        
        User savedUser = userRepository.save(user);
        
        log.info("User and Profile created successfully for email: {}", input.getEmail());

        return savedUser;
    }

    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getUsername(),
                        input.getPassword()
                )
        );

        return userRepository.findByUsername(input.getUsername())
                .orElseThrow();
    }
}