package com.flavourvault.flavour_vault_backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flavourvault.flavour_vault_backend.entities.Ingredient;
import com.flavourvault.flavour_vault_backend.entities.IngredientDetail;
import com.flavourvault.flavour_vault_backend.entities.Instruction;
import com.flavourvault.flavour_vault_backend.entities.Recipe;
import com.flavourvault.flavour_vault_backend.repository.IngredientRepository;
import com.flavourvault.flavour_vault_backend.repository.RecipeRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
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
        log.info("Creating recipe for: {}", recipe.getName());

        // Check each IngredientDetail for existing ingredients
        for (IngredientDetail detail : recipe.getIngredientDetails()) {
            Ingredient ingredient = detail.getIngredient();

            if (ingredient.getName() != null) {
                // Try to find the ingredient by name
                Optional<Ingredient> existingIngredient = ingredientRepository.findByName(ingredient.getName());
                
                if (existingIngredient.isPresent()) {
                    // Use the existing ingredient if found
                    detail.setIngredient(existingIngredient.get());
                } else {
                    // Save the new ingredient if it doesn't exist
                    Ingredient savedIngredient = ingredientRepository.save(ingredient);
                    detail.setIngredient(savedIngredient);
                }
            } else {
                log.warn("Ingredient name is null for one of the details.");
                throw new IllegalArgumentException("Ingredient name cannot be null.");
            }

            // Set the recipe reference in IngredientDetail
            detail.setRecipe(recipe);
        }

        // Set the recipe for each instruction to maintain the relationship
        for (Instruction instruction : recipe.getInstructions()) {
            instruction.setRecipe(recipe);
        }

        // Save the recipe and associated IngredientDetails
        return recipeRepository.save(recipe);
    }

	/**
	 * Retrieve a recipe by its ID.
	 * 
	 * @Cacheable annotation to add this to Redis
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
	@CacheEvict(value = "recipes", key = "#id")
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
	@CacheEvict(value = "recipes", key = "#id")
	public Recipe updateRecipe(Long id, Recipe updatedRecipe) {
	    log.info("Updating recipe with id: {} with new recipe data", id);

	    return recipeRepository.findById(id).map(existingRecipe -> {
	        // Update basic recipe details
	        existingRecipe.setName(updatedRecipe.getName());
	        existingRecipe.setDescription(updatedRecipe.getDescription());
	        existingRecipe.setPreparationTime(updatedRecipe.getPreparationTime());
	        existingRecipe.setCookingTime(updatedRecipe.getCookingTime());

	        // Update instructions
	        updateInstructions(existingRecipe, updatedRecipe.getInstructions());

	        // Update ingredient details
	        updateIngredientDetails(existingRecipe, updatedRecipe.getIngredientDetails());

	        log.info("Successfully updated recipe with id: {}", id);
	        return recipeRepository.save(existingRecipe); // Save changes
	    }).orElseThrow(() -> new IllegalArgumentException("Recipe with id " + id + " does not exist"));
	}

	private void updateInstructions(Recipe existingRecipe, List<Instruction> updatedInstructions) {
	    existingRecipe.getInstructions().removeIf(instr -> updatedInstructions.stream()
	        .noneMatch(updatedInstr -> updatedInstr.getId() != null && updatedInstr.getId().equals(instr.getId())));
	    
	    for (Instruction updatedInstr : updatedInstructions) {
	        if (updatedInstr.getId() != null) {
	            existingRecipe.getInstructions().stream()
	                .filter(instr -> instr.getId().equals(updatedInstr.getId()))
	                .findFirst()
	                .ifPresent(instr -> instr.setStep(updatedInstr.getStep()));
	        } else {
	            // For new instructions
	            updatedInstr.setRecipe(existingRecipe);
	            existingRecipe.getInstructions().add(updatedInstr);
	        }
	    }
	}

	private void updateIngredientDetails(Recipe existingRecipe, List<IngredientDetail> updatedDetails) {
	    existingRecipe.getIngredientDetails().removeIf(detail -> updatedDetails.stream()
	        .noneMatch(updatedDetail -> updatedDetail.getId() != null && updatedDetail.getId().equals(detail.getId())));

	    for (IngredientDetail updatedDetail : updatedDetails) {
	        Ingredient ingredient = updatedDetail.getIngredient();

	        // Check if the ingredient is transient (new and unsaved)
	        if (ingredient.getId() == null) {
	            // Save the new ingredient
	            Ingredient savedIngredient = ingredientRepository.save(ingredient);
	            updatedDetail.setIngredient(savedIngredient);
	        } else {
	            // Ensure the ingredient is attached by finding it in the database
	            Optional<Ingredient> existingIngredient = ingredientRepository.findById(ingredient.getId());
	            updatedDetail.setIngredient(existingIngredient.orElse(ingredient));
	        }

	        if (updatedDetail.getId() != null) {
	            existingRecipe.getIngredientDetails().stream()
	                .filter(detail -> detail.getId().equals(updatedDetail.getId()))
	                .findFirst()
	                .ifPresent(detail -> {
	                    detail.setQuantity(updatedDetail.getQuantity());
	                    detail.setUnit(updatedDetail.getUnit());
	                    detail.setPreparation(updatedDetail.getPreparation());
	                    detail.setIngredient(updatedDetail.getIngredient());
	                });
	        } else {
	            // For new ingredient details
	            updatedDetail.setRecipe(existingRecipe);
	            existingRecipe.getIngredientDetails().add(updatedDetail);
	        }
	    }
	}


}
