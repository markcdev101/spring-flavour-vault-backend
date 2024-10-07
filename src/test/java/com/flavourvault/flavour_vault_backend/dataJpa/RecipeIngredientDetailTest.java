package com.flavourvault.flavour_vault_backend.dataJpa;

import com.flavourvault.flavour_vault_backend.entities.Ingredient;
import com.flavourvault.flavour_vault_backend.entities.IngredientDetail;
import com.flavourvault.flavour_vault_backend.entities.Recipe;
import com.flavourvault.flavour_vault_backend.model.*;
import com.flavourvault.flavour_vault_backend.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test") 
public class RecipeIngredientDetailTest {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Test
    public void testRecipeWithPreparationAndCookingTimes() {
        // Create Ingredients
        Ingredient flour = new Ingredient();
        flour.setName("Flour");

        Ingredient sugar = new Ingredient();
        sugar.setName("Sugar");

        ingredientRepository.save(flour);
        ingredientRepository.save(sugar);

        // Create Recipe with preparation and cooking times
        Recipe cake = new Recipe();
        cake.setName("Cake");
        cake.setDescription("Delicious cake");
        cake.setPreparationTime(15);  // 15 minutes for preparation
        cake.setCookingTime(45);      // 45 minutes for cooking

        // Create Ingredient Details for the Recipe
        IngredientDetail flourDetail = new IngredientDetail();
        flourDetail.setQuantity("2");
        flourDetail.setUnit("cups");
        flourDetail.setPreparation("sifted");
        flourDetail.setIngredient(flour);
        flourDetail.setRecipe(cake);

        IngredientDetail sugarDetail = new IngredientDetail();
        sugarDetail.setQuantity("1");
        sugarDetail.setUnit("cup");
        sugarDetail.setPreparation("granulated");
        sugarDetail.setIngredient(sugar);
        sugarDetail.setRecipe(cake);

        cake.setIngredientDetails(List.of(flourDetail, sugarDetail));

        // Save Recipe with Ingredient Details
        recipeRepository.save(cake);

        // Fetch Recipe and Verify Ingredients, Preparation Time, and Cooking Time
        Recipe fetchedRecipe = recipeRepository.findById(cake.getId()).get();
        assertThat(fetchedRecipe.getIngredientDetails()).hasSize(2);
        assertThat(fetchedRecipe.getPreparationTime()).isEqualTo(15);
        assertThat(fetchedRecipe.getCookingTime()).isEqualTo(45);
    }
}

