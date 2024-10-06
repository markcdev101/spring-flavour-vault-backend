package com.flavourvault.flavour_vault_backend.service;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.flavourvault.flavour_vault_backend.dto.LoginUserDto;
import com.flavourvault.flavour_vault_backend.dto.RegisterUserDto;
import com.flavourvault.flavour_vault_backend.entities.Role;
import com.flavourvault.flavour_vault_backend.entities.User;
import com.flavourvault.flavour_vault_backend.model.RoleEnum;
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


    public User signup(RegisterUserDto input) {
    	Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER);
        
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

    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }
}