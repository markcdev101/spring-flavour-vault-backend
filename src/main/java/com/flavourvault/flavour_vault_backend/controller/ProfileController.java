package com.flavourvault.flavour_vault_backend.controller;

import com.flavourvault.flavour_vault_backend.dto.ProfileDto;
import com.flavourvault.flavour_vault_backend.dto.UserInfoDto;
import com.flavourvault.flavour_vault_backend.entities.Profile;
import com.flavourvault.flavour_vault_backend.entities.User;
import com.flavourvault.flavour_vault_backend.service.AuthenticationService;
import com.flavourvault.flavour_vault_backend.service.JwtService;
import com.flavourvault.flavour_vault_backend.service.ProfileService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/profile")
public class ProfileController {

	@Autowired
	private final AuthenticationService authenticationService;
	@Autowired
	private final JwtService jwtService;
	@Autowired
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService, AuthenticationService authenticationService, JwtService jwtService) {
        this.profileService = profileService;
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
    }

    // Updated endpoint to retrieve the profile of the currently authenticated user
    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile(@CookieValue(name = "jwtToken", required = false) String token) {
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
            profileDto.setEmail(profile.getEmail());
            profileDto.setFullName(profile.getFullName());
            userInfoDto.setProfile(profileDto);
        }

        return ResponseEntity.ok(userInfoDto);
    }


    // POST endpoint to create a new Profile (typically linked to a user creation)
    @PostMapping
    public ResponseEntity<Profile> createProfile(@RequestBody User user) {
        Profile profile = profileService.createProfile(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(profile);
    }

    // PUT endpoint to update an existing Profile
    @PutMapping("/{profileId}")
    public ResponseEntity<Profile> updateProfile(
            @PathVariable Integer profileId,
            @RequestBody Profile updatedProfile) {
        try {
            Profile profile = profileService.updateProfile(profileId, updatedProfile);
            return ResponseEntity.ok(profile);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
