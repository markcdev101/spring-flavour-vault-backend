package com.flavourvault.flavour_vault_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flavourvault.flavour_vault_backend.model.Recipe;
import com.flavourvault.flavour_vault_backend.service.RecipeManagementService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/flavourvault/api")
public class RecipeManagementController {

	/**
	 * @Autowired annotation used to inject dependency
	 */
	@Autowired
	private RecipeManagementService recipeManagementService;

	/**
	 * Gets all the recipe
	 * @GetMapping to indicate it is a GET method
	 * @return
	 */
	@GetMapping("/recipes")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN', 'SUPER_ADMIN')")
	public ResponseEntity<List<Recipe>> getAllRecipes() {
		log.info("START GET /recipes endpoint");
		List<Recipe> recipes = recipeManagementService.getAllRecipes();
		log.info("END GET /recipes endpoint");
		return ResponseEntity.ok(recipes);
	}


	/**
	 * Gets the recipe by id
	 * @GetMappingId to indicate it is a GET method
	 * @param id
	 * @return
	 */
	@GetMapping("/recipes/{id}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN', 'SUPER_ADMIN')")
	public ResponseEntity<Recipe> getRecipe(@PathVariable Long id) {
		log.info("START GET /recipes/{id} endpoint");
		Recipe recipe = recipeManagementService.getRecipe(id);
		if (recipe != null) {
			log.info("END GET /recipes/{id} endpoint");
			return ResponseEntity.ok(recipe);
		} else {
			log.info("END GET /recipes/{id} endpoint");
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * Creates a recipe
	 * @PostMapping annotation to indicate this is a POST method
	 * ResponseEntity adds HTTP Status Code
	 * @param recipe
	 * @return
	 */
	@PostMapping("/recipes")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Recipe> createRecipe(@RequestBody Recipe recipe) {
		log.info("START POST /recipes endpoint");
		Recipe createdRecipe = recipeManagementService.createRecipe(recipe);
		log.info("END POST /recipes endpoint");
		return ResponseEntity.ok(createdRecipe);
	}


	/**
	 * Update a recipe by ID
	 * @PutMapping to indicate it's a PUT method for updating
	 * @param id
	 * @param recipe
	 * @return ResponseEntity with the updated recipe
	 */
	@PutMapping("/recipes/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Recipe> updateRecipe(@PathVariable Long id, @RequestBody Recipe recipe) {
		log.info("START PUT /recipes/{id} endpoint");
		Recipe updatedRecipe = recipeManagementService.updateRecipe(id, recipe);
		if (updatedRecipe != null) {
			log.info("END PUT /recipes/{id} endpoint");
			return ResponseEntity.ok(updatedRecipe);
		} else {
			log.info("END PUT /recipes/{id} endpoint");
			return ResponseEntity.notFound().build();
		}
	}


	/**
	 * Delete the recipe by id
	 * @DeleteMapping to indicate it is a DELETE method.
	 * @param id
	 * @return
	 */
	@DeleteMapping("/recipes/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
		log.info("START DELETE /recipes/{id} endpoint");
		recipeManagementService.deleteRecipe(id);
		log.info("END DELETE /recipes/{id} endpoint");
		return ResponseEntity.noContent().build();
	}
}
