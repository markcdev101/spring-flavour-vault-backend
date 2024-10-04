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
import com.flavourvault.flavour_vault_backend.model.Ingredient;
import com.flavourvault.flavour_vault_backend.service.IngredientManagementService;

/**
 * Unit tests for IngredientManagementController
 */
@WebMvcTest(IngredientManagementController.class)
public class IngredientManagementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IngredientManagementService ingredientManagementService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private Ingredient ingredient1;
    private Ingredient ingredient2;

    @BeforeEach
    public void setup() {
        ingredient1 = new Ingredient(1L, "Onion", null);
        ingredient2 = new Ingredient(2L, "Garlic", null);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetAllIngredients() throws Exception {
        when(ingredientManagementService.getAllIngredients()).thenReturn(Arrays.asList(ingredient1, ingredient2));

        mockMvc.perform(get("/flavourvault/api/ingredients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Onion"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Garlic"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testCreateIngredient() throws Exception {
        Ingredient newIngredient = new Ingredient(null, "Tomato", null);

        when(ingredientManagementService.addIngredient(any(Ingredient.class))).thenReturn(ingredient1);

        mockMvc.perform(post("/flavourvault/api/ingredients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newIngredient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Onion"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetIngredientById_Success() throws Exception {
        when(ingredientManagementService.getIngredient(1L)).thenReturn(ingredient1);

        mockMvc.perform(get("/flavourvault/api/ingredients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Onion"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetIngredientById_NotFound() throws Exception {
        when(ingredientManagementService.getIngredient(1L)).thenReturn(null);

        mockMvc.perform(get("/flavourvault/api/ingredients/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testUpdateIngredient_Success() throws Exception {
        Ingredient updatedIngredient = new Ingredient(1L, "Updated Onion", null);

        when(ingredientManagementService.updateIngredient(eq(1L), any(Ingredient.class))).thenReturn(updatedIngredient);

        mockMvc.perform(put("/flavourvault/api/ingredients/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedIngredient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Updated Onion"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testUpdateIngredient_NotFound() throws Exception {
        when(ingredientManagementService.updateIngredient(eq(1L), any(Ingredient.class))).thenReturn(null);

        mockMvc.perform(put("/flavourvault/api/ingredients/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Ingredient())))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testDeleteIngredient() throws Exception {
        doNothing().when(ingredientManagementService).deleteIngredient(1L);

        mockMvc.perform(delete("/flavourvault/api/ingredients/1"))
                .andExpect(status().isNoContent());
    }
}
