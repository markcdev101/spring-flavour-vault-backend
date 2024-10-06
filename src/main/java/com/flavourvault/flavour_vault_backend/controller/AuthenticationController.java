package com.flavourvault.flavour_vault_backend.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flavourvault.flavour_vault_backend.dto.LoginResponse;
import com.flavourvault.flavour_vault_backend.dto.LoginUserDto;
import com.flavourvault.flavour_vault_backend.dto.RegisterUserDto;
import com.flavourvault.flavour_vault_backend.entities.User;
import com.flavourvault.flavour_vault_backend.service.AuthenticationService;
import com.flavourvault.flavour_vault_backend.service.JwtService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/auth")
@RestController
public class AuthenticationController {
	@Autowired
	private final JwtService jwtService;

	@Autowired
	private final AuthenticationService authenticationService;

	public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
		this.jwtService = jwtService;
		this.authenticationService = authenticationService;
	}

	@PostMapping("/signup")
	public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
		User registeredUser = authenticationService.signup(registerUserDto);

		return ResponseEntity.ok(registeredUser);
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
		User authenticatedUser = authenticationService.authenticate(loginUserDto);

		String jwtToken = jwtService.generateToken(authenticatedUser);

		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setToken(jwtToken);
		loginResponse.setExpiresIn(jwtService.getExpirationTime());

		return ResponseEntity.ok(loginResponse);
	}
}