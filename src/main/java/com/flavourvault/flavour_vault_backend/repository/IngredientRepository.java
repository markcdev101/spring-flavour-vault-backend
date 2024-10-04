package com.flavourvault.flavour_vault_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.flavourvault.flavour_vault_backend.model.Ingredient;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
	
	Optional<Ingredient> findByName(String name);
}
