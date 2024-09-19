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
		if (recipeRepository.existsById(id)) {
	        recipeRepository.deleteById(id);
	    } else {
	        // Handle the case where the recipe doesn't exist, e.g., throw an exception
	        throw new IllegalArgumentException("Recipe with id " + id + " does not exist.");
	    }
	}
	
	
	/**
     * Update an existing recipe
     * @param id
     * @param updatedRecipe
     * @return the updated recipe or null if recipe doesn't exist
     */
    public Recipe updateRecipe(Long id, Recipe updatedRecipe) {
        return recipeRepository.findById(id).map(recipe -> {
            // Update the fields that can be changed
            recipe.setName(updatedRecipe.getName());
            recipe.setIngredients(updatedRecipe.getIngredients());
            recipe.setInstructions(updatedRecipe.getInstructions());
            // Add more fields as needed

            return recipeRepository.save(recipe);
        }).orElse(null);  // Return null if the recipe doesn't exist
    }
	
	
	
}
