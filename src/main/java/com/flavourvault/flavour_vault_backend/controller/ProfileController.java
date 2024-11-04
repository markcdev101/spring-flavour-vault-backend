package com.flavourvault.flavour_vault_backend.controller;

import com.flavourvault.flavour_vault_backend.entities.Profile;
import com.flavourvault.flavour_vault_backend.entities.User;
import com.flavourvault.flavour_vault_backend.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    // GET endpoint to retrieve a Profile by Profile ID
    @GetMapping("/{profileId}")
    public ResponseEntity<Profile> getProfileById(@PathVariable Integer profileId) {
        Optional<Profile> profile = profileService.findById(profileId);
        return profile.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
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
