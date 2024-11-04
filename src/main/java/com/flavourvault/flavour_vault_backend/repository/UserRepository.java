package com.flavourvault.flavour_vault_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flavourvault.flavour_vault_backend.entities.User;

/**
 * Repository for User to represents the Data access layer of the User entity
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	Optional<User> findByUsername(String username);
}
