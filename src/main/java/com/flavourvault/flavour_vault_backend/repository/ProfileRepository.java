package com.flavourvault.flavour_vault_backend.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flavourvault.flavour_vault_backend.entities.Profile;

/**
 * Repository for User to represents the Data access layer of the User entity
 */
@Repository
public interface ProfileRepository extends JpaRepository<Profile, Integer> {
	Optional<Profile> findByEmail(String email);
}
