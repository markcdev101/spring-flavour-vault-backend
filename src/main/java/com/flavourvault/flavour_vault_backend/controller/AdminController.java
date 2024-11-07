package com.flavourvault.flavour_vault_backend.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flavourvault.flavour_vault_backend.dto.RegisterUserDto;
import com.flavourvault.flavour_vault_backend.entities.User;
import com.flavourvault.flavour_vault_backend.service.JwtService;
import com.flavourvault.flavour_vault_backend.service.UserService;

@RequestMapping("/admins")
@RestController
public class AdminController {
    private final UserService userService;
    
    @Autowired
	private final JwtService jwtService;

    public AdminController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }
    
    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<User> createAdministrator(@CookieValue(name = "jwtToken", required = false) String token, 
    		@RequestBody RegisterUserDto registerUserDto) {
    	if (token == null || !jwtService.isTokenValid(token)) {
			return ResponseEntity.status(401).body(null);
		}
    	User createdAdmin = userService.createAdministrator(registerUserDto);

        return ResponseEntity.ok(createdAdmin);
    }
}
