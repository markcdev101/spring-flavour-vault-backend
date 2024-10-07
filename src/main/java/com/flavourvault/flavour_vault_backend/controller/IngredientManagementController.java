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

import com.flavourvault.flavour_vault_backend.entities.Ingredient;
import com.flavourvault.flavour_vault_backend.service.IngredientManagementService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/flavourvault/api")
public class IngredientManagementController {

	@Autowired
	private IngredientManagementService ingredientManagementService;

	/**
	 * Gets all the ingredients
	 * @GetMapping to indicate it is a GET method
	 * @return
	 */
	@GetMapping("/ingredients")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN', 'SUPER_ADMIN')")
	public ResponseEntity<List<Ingredient>> getAllIngredients() {
		log.info("START GET /ingredients endpoint");
		List<Ingredient> ingredients = ingredientManagementService.getAllIngredients();
		log.info("END GET /ingredients endpoint");
		return ResponseEntity.ok(ingredients);
	}


	/**
	 * Gets the ingredient by id
	 * @param id
	 * @return
	 */
	@GetMapping("/ingredients/{id}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN', 'SUPER_ADMIN')")
	public ResponseEntity<Ingredient> getIngredient(@PathVariable Long id) {
		log.info("START GET /ingredient/{id} endpoint");
		Ingredient ingredient = ingredientManagementService.getIngredient(id);
		if(ingredient != null) {
			log.info("END GET /ingredient/{id} endpoint");
			return ResponseEntity.ok(ingredient);
		} else {
			log.info("END GET /ingredient/{id} endpoint");
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * Creates a new ingredient
	 * @PostMapping annotation to indicate this is a POST method
	 * ResponseEntity adds HTTP Status Code
	 * @param ingredient
	 * @return
	 */
	@PostMapping("/ingredients")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Ingredient> createIngredient(@RequestBody Ingredient ingredient) {
		log.info("START POST /ingredient endpoint");
		Ingredient newIngredient = ingredientManagementService.addIngredient(ingredient);
		log.info("END POST /ingredient endpoint");
		return ResponseEntity.ok(newIngredient);
	}



	/**
	 * Update an existing ingredient
	 * @param id
	 * @param ingredient
	 * @return
	 */
	@PutMapping("/ingredients/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Ingredient> updateIngredient(@PathVariable Long id, @RequestBody Ingredient ingredient) {
		log.info("START PUT /ingredient/{id} endpoint");
		Ingredient updatedIngredient = ingredientManagementService.updateIngredient(id, ingredient);
		if(updatedIngredient != null) {
			log.info("END PUT /ingredient/{id} endpoint");
			return ResponseEntity.ok(updatedIngredient);
		} else {
			log.info("END PUT /ingredient/{id} endpoint");
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * Delete the recipe by id
	 * @param id
	 * @return
	 */
	@DeleteMapping("/ingredients/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
		log.info("START DELETE /ingredient/{id} endpoint");
		ingredientManagementService.deleteIngredient(id);
		log.info("END DELETE /ingredient/{id} endpoint");
		return ResponseEntity.noContent().build();
	}


}
