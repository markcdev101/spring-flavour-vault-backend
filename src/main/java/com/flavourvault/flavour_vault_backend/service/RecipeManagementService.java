package com.flavourvault.flavour_vault_backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flavourvault.flavour_vault_backend.model.IngredientDetail;
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
	
	/**
     * Create a new recipe with its ingredients and details.
     * 
     * @param recipe The recipe to create.
     * @return The created recipe.
     */
	public Recipe createRecipe(Recipe recipe) {
	    // Ensure IngredientDetail relationship is maintained
	    for (IngredientDetail detail : recipe.getIngredientDetails()) {
	        detail.setRecipe(recipe);  // Set the recipe reference in IngredientDetail
	    }
	    return recipeRepository.save(recipe);
	}
	
	/**
     * Retrieve a recipe by its ID.
     * 
     * @param id The ID of the recipe to retrieve.
     * @return The recipe, or null if not found.
     */
	public Recipe getRecipe(Long id) {
		return recipeRepository.findById(id).orElse(null);
	}
	
	/**
     * Retrieve all recipes.
     * 
     * @return A list of all recipes.
     */
	public List<Recipe> getAllRecipes() {
		return recipeRepository.findAll();
	}
	
	/**
     * Delete a recipe by its ID.
     * 
     * @param id The ID of the recipe to delete.
     */
	public void deleteRecipe(Long id) {
		if (recipeRepository.existsById(id)) {
			recipeRepository.deleteById(id);
		} else {
			throw new IllegalArgumentException("Recipe with id " + id + " does not exist.");
		}
	}
	
	/**
     * Update an existing recipe.
     * 
     * @param id The ID of the recipe to update.
     * @param updatedRecipe The updated recipe data.
     * @return The updated recipe, or null if the recipe doesn't exist.
     */
    public Recipe updateRecipe(Long id, Recipe updatedRecipe) {
        return recipeRepository.findById(id).map(existingRecipe -> {
            // Update basic recipe details
            existingRecipe.setName(updatedRecipe.getName());
            existingRecipe.setDescription(updatedRecipe.getDescription());
            existingRecipe.setPreparationTime(updatedRecipe.getPreparationTime());
            existingRecipe.setCookingTime(updatedRecipe.getCookingTime());

            // Update the instructions
            existingRecipe.setInstructions(updatedRecipe.getInstructions());

            // Clear and update IngredientDetails
            existingRecipe.getIngredientDetails().clear();  // Remove existing ingredient details
            for (IngredientDetail detail : updatedRecipe.getIngredientDetails()) {
                detail.setRecipe(existingRecipe);  // Set the correct reference
                existingRecipe.getIngredientDetails().add(detail);
            }

            return recipeRepository.save(existingRecipe);
        }).orElse(null);  // Return null if the recipe doesn't exist
    }
	
	
	
}
