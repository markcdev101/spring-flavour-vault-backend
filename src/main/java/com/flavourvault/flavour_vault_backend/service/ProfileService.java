package com.flavourvault.flavour_vault_backend.service;

import com.flavourvault.flavour_vault_backend.entities.Profile;
import com.flavourvault.flavour_vault_backend.entities.User;
import com.flavourvault.flavour_vault_backend.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;

    @Autowired
    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    // Method to create a Profile and link it with a User
    public Profile createProfile(User user) {
        Profile profile = new Profile();
        profile.setUser(user);
        profile.setUsername(user.getUsername()); // using email as username for example
        return profileRepository.save(profile);
    }

    // Method to update a Profile
    public Profile updateProfile(Integer profileId, Profile updatedProfile) {
        return profileRepository.findById(profileId)
                .map(profile -> {
                    profile.setUsername(updatedProfile.getUsername());
                    return profileRepository.save(profile);
                })
                .orElseThrow(() -> new RuntimeException("Profile not found with ID: " + profileId));
    }

    // Method to fetch a Profile by User ID
    public Profile findByUserId(Integer userId) {
        return profileRepository.findByUserId(userId);
    }

    // Method to fetch a Profile by Profile ID
    public Optional<Profile> findById(Integer profileId) {
        return profileRepository.findById(profileId);
    }
}
