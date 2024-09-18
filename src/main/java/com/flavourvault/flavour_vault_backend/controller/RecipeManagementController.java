package com.flavourvault.flavour_vault_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flavourvault.flavour_vault_backend.model.Recipe;
import com.flavourvault.flavour_vault_backend.service.RecipeManagementService;

@RestController
@RequestMapping("/flavourvault/api/recipes")
public class RecipeManagementController {

	/**
	 * @Autowired annotation used to inject dependency
	 */
	@Autowired
	private RecipeManagementService recipeManagementService;

	/**
	 * Creates a recipe
	 * @PostMapping annotation to indicate this is a POST method
	 * ResponseEntity adds HTTP Status Code
	 * @param recipe
	 * @return
	 */
	@PostMapping
	public ResponseEntity<Recipe> createRecipe(@RequestBody Recipe recipe) {
		Recipe createdRecipe = recipeManagementService.createRecipe(recipe);
		return ResponseEntity.ok(createdRecipe);
	}

	/**
	 * Gets the recipe by id
	 * @GetMappingId to indicate it is a GET method
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Recipe> getRecipe(@PathVariable Long id) {
		Recipe recipe = recipeManagementService.getRecipe(id);
		if (recipe != null) {
			return ResponseEntity.ok(recipe);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * Gets all the recipe
	 * @GetMapping to indicate it is a GET method
	 * @return
	 */
	@GetMapping
	public ResponseEntity<List<Recipe>> getAllRecipes() {
		List<Recipe> recipes = recipeManagementService.getAllRecipes();
		return ResponseEntity.ok(recipes);
	}

	
	/**
	 * Delete the recipe by id
	 * @DeleteMapping to indicate it is a DELETE method.
	 * @param id
	 * @return
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
		recipeManagementService.deleteRecipe(id);
		return ResponseEntity.noContent().build();
	}


}
