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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flavourvault.flavour_vault_backend.model.Recipe;
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
		recipe1 = new Recipe(1L, "Recipe 1", "Description 1", "Ingredients 1", "Instructions 1");
        recipe2 = new Recipe(2L, "Recipe 2", "Description 2", "Ingredients 2", "Instructions 2");
        
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	
	@Test
	@WithMockUser(roles = "USER")
    public void testGetAllRecipes() throws Exception {
        when(recipeManagementService.getAllRecipes()).thenReturn(Arrays.asList(recipe1, recipe2));

        mockMvc.perform(get("/flavourvault/api/recipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Recipe 1"))
                .andExpect(jsonPath("$[0].description").value("Description 1"))
                .andExpect(jsonPath("$[0].ingredients").value("Ingredients 1"))
                .andExpect(jsonPath("$[0].instructions").value("Instructions 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Recipe 2"))
                .andExpect(jsonPath("$[1].description").value("Description 2"))
                .andExpect(jsonPath("$[1].ingredients").value("Ingredients 2"))
                .andExpect(jsonPath("$[1].instructions").value("Instructions 2"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testCreateRecipe() throws Exception {
        Recipe newRecipe = new Recipe(null, "Recipe 3", "Description 3", "Ingredients 3", "Instructions 3");

        when(recipeManagementService.createRecipe(any(Recipe.class))).thenReturn(recipe1);

        mockMvc.perform(post("/flavourvault/api/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newRecipe)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Recipe 1"))
                .andExpect(jsonPath("$.description").value("Description 1"))
                .andExpect(jsonPath("$.ingredients").value("Ingredients 1"))
                .andExpect(jsonPath("$.instructions").value("Instructions 1"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetRecipeById_Success() throws Exception {
        when(recipeManagementService.getRecipe(1L)).thenReturn(recipe1);

        mockMvc.perform(get("/flavourvault/api/recipes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Recipe 1"))
                .andExpect(jsonPath("$.description").value("Description 1"))
                .andExpect(jsonPath("$.ingredients").value("Ingredients 1"))
                .andExpect(jsonPath("$.instructions").value("Instructions 1"));
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
        Recipe updatedRecipe = new Recipe(1L, "Updated Recipe", "Updated Description", "Updated Ingredients", "Updated Instructions");

        when(recipeManagementService.updateRecipe(eq(1L), any(Recipe.class))).thenReturn(updatedRecipe);

        mockMvc.perform(put("/flavourvault/api/recipes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedRecipe)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Updated Recipe"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.ingredients").value("Updated Ingredients"))
                .andExpect(jsonPath("$.instructions").value("Updated Instructions"));
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
