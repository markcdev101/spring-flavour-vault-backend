package com.flavourvault.flavour_vault_backend.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flavourvault.flavour_vault_backend.dto.LoginResponse;
import com.flavourvault.flavour_vault_backend.dto.LoginUserDto;
import com.flavourvault.flavour_vault_backend.dto.ProfileDto;
import com.flavourvault.flavour_vault_backend.dto.RegisterUserDto;
import com.flavourvault.flavour_vault_backend.dto.UserInfoDto;
import com.flavourvault.flavour_vault_backend.entities.Profile;
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
	public ResponseEntity<?> authenticate(@RequestBody LoginUserDto loginUserDto) {
		User authenticatedUser = authenticationService.authenticate(loginUserDto);

		// Generate JWT token
		String jwtToken = jwtService.generateToken(authenticatedUser);

		// Set the token in an HTTP-only cookie
        ResponseCookie jwtCookie = ResponseCookie.from("jwtToken", jwtToken)
                .httpOnly(true)
                .path("/")
                .maxAge(jwtService.getExpirationTime() / 1000) // Set max age in seconds
                .sameSite("Strict") //If cookie is set to String it will not be sent for cross-origin requests
                .secure(false) // Ensure the cookie is secure (only over HTTPS)
                .build();

        // Set the username in another HTTP-only cookie
        ResponseCookie usernameCookie = ResponseCookie.from("username", authenticatedUser.getUsername())
                .httpOnly(true)
                .path("/")
                .maxAge(jwtService.getExpirationTime() / 1000)
                .sameSite("Strict") //If cookie is set to String it will not be sent for cross-origin requests
                .secure(false)
                .build();

        // Set cookies in the response header
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, usernameCookie.toString())
                .body("Login successful");
	}
	
	  @PostMapping("/logout")
	    public ResponseEntity<?> logout() {
	        // Create expired cookies to clear the JWT token and username cookies
	        ResponseCookie jwtCookie = ResponseCookie.from("jwtToken", "")
	                .httpOnly(true)
	                .path("/")
	                .maxAge(0) // Immediately expire the cookie
	                .sameSite("None") //If cookie is set to String it will not be sent for cross-origin requests
	                .secure(false)
	                .build();

	        ResponseCookie usernameCookie = ResponseCookie.from("username", "")
	                .httpOnly(true)
	                .path("/")
	                .maxAge(0)
	                .sameSite("None") //If cookie is set to String it will not be sent for cross-origin requests
	                .secure(false)
	                .build();

	        return ResponseEntity.ok()
	                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
	                .header(HttpHeaders.SET_COOKIE, usernameCookie.toString())
	                .body("Logged out successfully");
	    }
	
	@GetMapping("/me")
	public ResponseEntity<?> getAuthenticatedUser(@CookieValue(name = "jwtToken", required = false) String token) {
		if (token == null || !jwtService.isTokenValid(token)) {
			return ResponseEntity.status(401).body("Unauthorized");
		}

		// Decode token to get user info
		String username = jwtService.extractUsername(token);
		User user = authenticationService.getUserByUsername(username);
		if (user == null) {
			return ResponseEntity.status(401).body("Unauthorized");
		}
		
		UserInfoDto userInfoDto = new UserInfoDto();
		userInfoDto.setUsername(user.getUsername());
		userInfoDto.setId(user.getId());
		userInfoDto.setRole(user.getRole().getName().toString());

		
		Profile profile = user.getProfile();
	    if (profile != null) {
	        ProfileDto profileDto = new ProfileDto();
//	        profileDto.setId(profile.getId());
	        profileDto.setEmail(profile.getEmail());
	        profileDto.setFullName(profile.getFullName());
	        // Map other necessary fields

	        userInfoDto.setProfile(profileDto);
	    }

		// Send user profile or limited information as needed
		return ResponseEntity.ok(userInfoDto);
	}
}