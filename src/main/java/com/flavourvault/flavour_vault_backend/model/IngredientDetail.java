package com.flavourvault.flavour_vault_backend.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="ingredient_details")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class IngredientDetail implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 8155803006869383664L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String quantity;  // e.g., "2 cups"
    private String unit;      // e.g., "cups", "tablespoons"
    private String preparation;  // e.g., "minced", "chopped", "diced"

    /**
     * @ManyToOne to indicate that you can have many ingredient_detail per 
     * recipe
     * @JsonIgnore prevents the recipe from being serialized in the IngredientDetail
     * 
     * i.e. You have 1g chopped onion and also 2g minced onion in one recipe.
     * i.e. You have 20g chopped oreos and also 5 whole oreos in one recipe.
     */
    @ManyToOne
    @JoinColumn(name = "recipe_id")
//    @JsonIgnore
    @JsonBackReference // This side of the relationship is ignored.
    private Recipe recipe;

    
    /**
     * @ManyToOne to indicate that you can have many ingredient_detail per 
     * ingredient
     * 
     * i.e. You can have an Onion as an ingredient
     * and have ingredient_detail as - 1g chopped, 2g minced
     */
    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;
}
