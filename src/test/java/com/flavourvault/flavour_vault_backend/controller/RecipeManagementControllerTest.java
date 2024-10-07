package com.flavourvault.flavour_vault_backend.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flavourvault.flavour_vault_backend.entities.Ingredient;
import com.flavourvault.flavour_vault_backend.entities.IngredientDetail;
import com.flavourvault.flavour_vault_backend.entities.Recipe;
import com.flavourvault.flavour_vault_backend.service.RecipeManagementService;

/**
 * @WebMvcTest is used to create a Spring text context that focuses on web components (controller layer)
 */
@WebMvcTest(RecipeManagementController.class)
public class RecipeManagementControllerTest {
	
	/**
	 * @Autowired for dependency injection
	 * MockMvc allows to simulate HTTP Requests and test the controller without needing to start the application
	 */
	@Autowired
	private MockMvc mockMvc;
	
	/**
	 * @MockBean mocks the RecipeManagementService which is injected into the controller
	 */
	@MockBean
	private RecipeManagementService recipeManagementService;
	
	/**
	 * @Autowired for dependency injection
	 * ObjectMapper is used to convert objects into JSON format for the request body.
	 */
	@Autowired
	private ObjectMapper objectMapper;
	
	private Recipe recipe1;
    private Recipe recipe2;
	
    /**
	 * @Autowired for dependency injection
	 * WebApplicationContext is an Interface to provide configuration for a web application.
	 */
    @Autowired
    private WebApplicationContext webApplicationContext;
	
	/**
	 * @BeforeEach indicates to run this method before each test
	 * MockitoAnnotations initializes fields annotated with Mockito annotations
	 * 
	 * Using webAppContextSetup to initialize MockMvc with full Spring context, 
	 * ensuring that security and other global configurations are applied in tests.
	 */
    @BeforeEach
    public void setup() {
        // Create Ingredient Details
        Ingredient flour = new Ingredient(1L, "Flour", null);
        IngredientDetail flourDetail = new IngredientDetail(1L, "2", "cups", "sifted", null, flour);

        Ingredient sugar = new Ingredient(2L, "Sugar", null);
        IngredientDetail sugarDetail = new IngredientDetail(2L, "1", "cup", "granulated", null, sugar);

        List<IngredientDetail> ingredientDetails = Arrays.asList(flourDetail, sugarDetail);

        // Create Recipe 1
        recipe1 = new Recipe(1L, "Chocolate Cake", "Delicious chocolate cake", 15, 60, ingredientDetails, null);

        // Create Recipe 2
        recipe2 = new Recipe(2L, "Vanilla Cake", "Delicious vanilla cake", 20, 50, ingredientDetails, null);
        
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
	
	
    @Test
    @WithMockUser(roles = "USER")
    public void testGetAllRecipes() throws Exception {
        when(recipeManagementService.getAllRecipes()).thenReturn(Arrays.asList(recipe1, recipe2));

        mockMvc.perform(get("/flavourvault/api/recipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Chocolate Cake"))
                .andExpect(jsonPath("$[0].description").value("Delicious chocolate cake"))
                .andExpect(jsonPath("$[0].preparationTime").value(15))
                .andExpect(jsonPath("$[0].cookingTime").value(60))
                .andExpect(jsonPath("$[0].ingredientDetails[0].quantity").value("2"))
                .andExpect(jsonPath("$[0].ingredientDetails[0].unit").value("cups"))
                .andExpect(jsonPath("$[0].ingredientDetails[0].preparation").value("sifted"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Vanilla Cake"))
                .andExpect(jsonPath("$[1].description").value("Delicious vanilla cake"))
                .andExpect(jsonPath("$[1].preparationTime").value(20))
                .andExpect(jsonPath("$[1].cookingTime").value(50));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testCreateRecipe() throws Exception {
        Recipe newRecipe = new Recipe(null, "Strawberry Cake", "Delicious strawberry cake", 10, 40, recipe1.getIngredientDetails(), null);

        when(recipeManagementService.createRecipe(any(Recipe.class))).thenReturn(recipe1);

        mockMvc.perform(post("/flavourvault/api/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newRecipe)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Chocolate Cake"))
                .andExpect(jsonPath("$.description").value("Delicious chocolate cake"))
                .andExpect(jsonPath("$.preparationTime").value(15))
                .andExpect(jsonPath("$.cookingTime").value(60));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetRecipeById_Success() throws Exception {
        when(recipeManagementService.getRecipe(1L)).thenReturn(recipe1);

        mockMvc.perform(get("/flavourvault/api/recipes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Chocolate Cake"))
                .andExpect(jsonPath("$.description").value("Delicious chocolate cake"))
                .andExpect(jsonPath("$.preparationTime").value(15))
                .andExpect(jsonPath("$.cookingTime").value(60));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetRecipeById_NotFound() throws Exception {
        when(recipeManagementService.getRecipe(1L)).thenReturn(null);

        mockMvc.perform(get("/flavourvault/api/recipes/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testUpdateRecipe_Success() throws Exception {
        Recipe updatedRecipe = new Recipe(1L, "Updated Chocolate Cake", "Updated Description", 20, 70, recipe1.getIngredientDetails(), null);

        when(recipeManagementService.updateRecipe(eq(1L), any(Recipe.class))).thenReturn(updatedRecipe);

        mockMvc.perform(put("/flavourvault/api/recipes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedRecipe)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Updated Chocolate Cake"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.preparationTime").value(20))
                .andExpect(jsonPath("$.cookingTime").value(70));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testUpdateRecipe_NotFound() throws Exception {
        when(recipeManagementService.updateRecipe(eq(1L), any(Recipe.class))).thenReturn(null);

        mockMvc.perform(put("/flavourvault/api/recipes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Recipe())))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testDeleteRecipe() throws Exception {
        doNothing().when(recipeManagementService).deleteRecipe(1L);

        mockMvc.perform(delete("/flavourvault/api/recipes/1"))
                .andExpect(status().isNoContent());
    }
}
