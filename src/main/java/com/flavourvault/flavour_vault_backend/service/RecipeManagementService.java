package com.flavourvault.flavour_vault_backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flavourvault.flavour_vault_backend.model.Recipe;
import com.flavourvault.flavour_vault_backend.repository.RecipeRepository;

@Service
public class RecipeManagementService {
	
	/**
	 * @Autowired annotation injects the dependency. Tells spring to inject an instance of a class into another class.
	 */
	@Autowired
	private RecipeRepository recipeRepository;
	
	
	//Business Logic
	
	public Recipe createRecipe(Recipe recipe) {
		return recipeRepository.save(recipe);
	}
	
	public Recipe getRecipe(Long id) {
		return recipeRepository.findById(id).orElse(null);
	}
	
	public List<Recipe> getAllRecipes() {
		return recipeRepository.findAll();
	}
	
	public void deleteRecipe(Long id) {
		
	}
	
	
}
