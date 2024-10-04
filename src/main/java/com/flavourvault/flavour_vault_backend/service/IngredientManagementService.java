package com.flavourvault.flavour_vault_backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flavourvault.flavour_vault_backend.model.Ingredient;
import com.flavourvault.flavour_vault_backend.repository.IngredientRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class IngredientManagementService {
	
	@Autowired
	private IngredientRepository ingredientRepository;
	
	
	//Business Logic
	/**
	 * Add a new ingredient and details
	 * @param ingredient
	 * @return
	 */
	public Ingredient addIngredient(Ingredient ingredient) {
		log.info("Adding ingredient for : {}", ingredient.getName());
		//TODO Validation to check whether ingredient exist or not
		Optional<Ingredient> existingIngredient = ingredientRepository.findByName(ingredient.getName());
		
		if(existingIngredient.isPresent()) {
			log.warn("Ingredient with name '{}' already exist.");
			throw new IllegalArgumentException("Ingredient with name " + ingredient.getName() + " already exists.");
		}
		
		
		return ingredientRepository.save(ingredient);
	}
	
	
	/**
	 * Retrieve an ingredient by its ID.
	 * @param id
	 * @return
	 */
	public Ingredient getIngredient(Long id) {
		log.info("Fetching ingredient with id: {}", id);
		
		return ingredientRepository.findById(id).orElse(null);
	}
	
	/**
	 * Retrieve all ingredients
	 * @return
	 */
	public List<Ingredient> getAllIngredients(){
		log.info("Fetching all ingredients");
		return ingredientRepository.findAll();
	}
	
	/**
	 * Delete an ingredient by its id
	 * @param id
	 */
	public void deleteIngredient(Long id){
		log.info("Deleting ingredient with id: {}", id);
		if(ingredientRepository.existsById(id)) {
			ingredientRepository.deleteById(id);
			log.info("Deleted ingredient with id: {}", id);
		} else {
			log.error("Error occured deleting ingredient with id: {}", id);
			throw new IllegalArgumentException("Recipe with id " + id + " does not exist.");
		}
	}
	
	/**
	 * Update an existing ingredient
	 * @param id
	 * @param updatedIngredient
	 * @return
	 */
	public Ingredient updateIngredient(Long id, Ingredient updatedIngredient) {
		log.info("Updating ingredient with id: {}", id);
		
		Ingredient existingIngredient = ingredientRepository.findById(id).orElse(null);
		return ingredientRepository.save(existingIngredient);

	}
}
