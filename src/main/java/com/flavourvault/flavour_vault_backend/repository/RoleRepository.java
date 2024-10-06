package com.flavourvault.flavour_vault_backend.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.flavourvault.flavour_vault_backend.model.Role;
import com.flavourvault.flavour_vault_backend.model.RoleEnum;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
    Optional<Role> findByName(RoleEnum name);
}
