package com.flavourvault.flavour_vault_backend.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.flavourvault.flavour_vault_backend.entities.Profile;

/**
 * Repository for User to represents the Data access layer of the User entity
 */
@Repository
public interface ProfileRepository extends CrudRepository<Profile, Integer> {
}
