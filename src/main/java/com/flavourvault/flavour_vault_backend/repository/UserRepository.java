package com.flavourvault.flavour_vault_backend.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.flavourvault.flavour_vault_backend.model.User;

/**
 * Repository for User to represents the Data access layer of the User entity
 */
@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
	Optional<User> findByEmail(String email);
}
