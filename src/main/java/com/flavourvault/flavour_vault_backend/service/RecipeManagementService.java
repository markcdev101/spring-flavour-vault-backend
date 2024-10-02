package com.flavourvault.flavour_vault_backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.flavourvault.flavour_vault_backend.model.Ingredient;
import com.flavourvault.flavour_vault_backend.model.IngredientDetail;
import com.flavourvault.flavour_vault_backend.model.Recipe;
import com.flavourvault.flavour_vault_backend.repository.IngredientRepository;
import com.flavourvault.flavour_vault_backend.repository.RecipeRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RecipeManagementService {
	
	/**
	 * @Autowired annotation injects the dependency. Tells spring to inject an instance of a class into another class.
	 */
	@Autowired
	private RecipeRepository recipeRepository;
	
	@Autowired
    private IngredientRepository ingredientRepository;
	
	
	//Business Logic
	
	 /**
     * Create a new recipe with its ingredients and details.
     * 
     * @param recipe The recipe to create.
     * @return The created recipe.
     */
    public Recipe createRecipe(Recipe recipe) {
    	log.info("Creating recipe for : {}", recipe.getName());
        // Save Ingredients if they don't already exist
        for (IngredientDetail detail : recipe.getIngredientDetails()) {
            Ingredient ingredient = detail.getIngredient();
            if (ingredient.getId() == null || !ingredientRepository.existsById(ingredient.getId())) {
                // Save the ingredient first if it doesn't exist
                Ingredient savedIngredient = ingredientRepository.save(ingredient);
                detail.setIngredient(savedIngredient);
            }
            detail.setRecipe(recipe);  // Set the recipe reference in IngredientDetail
        }
        
        // Save the recipe and associated IngredientDetails
        return recipeRepository.save(recipe);
    }
	
	/**
     * Retrieve a recipe by its ID.
     * 
     * @param id The ID of the recipe to retrieve.
     * @return The recipe, or null if not found.
     */
    @Cacheable(value = "recipes", key = "#id")
	public Recipe getRecipe(Long id) {
    	log.info("Fetching recipe with id : {}", id);
    	
    	log.info("Cache miss - retrieving from database");
		return recipeRepository.findById(id).orElse(null);
	}
	
	/**
     * Retrieve all recipes.
     * 
     * @return A list of all recipes.
     */
	public List<Recipe> getAllRecipes() {
    	log.info("Fetching all recipes");
		return recipeRepository.findAll();
	}
	
	/**
     * Delete a recipe by its ID.
     * 
     * @param id The ID of the recipe to delete.
     */
	public void deleteRecipe(Long id) {
    	log.info("Deleting recipe with id : {}", id);
		if (recipeRepository.existsById(id)) {
			recipeRepository.deleteById(id);
			log.info("Deleted recipe with id : {}", id);
		} else {
			log.error("Error occured deleting recipe with id : {}", id);
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
    	log.info("Updating recipe with id : {} with a new recipe: {}", id, updatedRecipe.getName());
    	
    	
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
            log.info("Updated recipe with id : {}", id);
            return recipeRepository.save(existingRecipe);
        }).orElse(null);  // Return null if the recipe doesn't exist
    }
	
	
	
}
