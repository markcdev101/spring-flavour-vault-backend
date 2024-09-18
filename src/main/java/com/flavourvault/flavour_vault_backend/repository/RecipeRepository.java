package com.flavourvault.flavour_vault_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.flavourvault.flavour_vault_backend.model.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

}
