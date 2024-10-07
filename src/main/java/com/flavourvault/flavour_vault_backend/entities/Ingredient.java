package com.flavourvault.flavour_vault_backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "ingredients")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Ingredient implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5597930343032084907L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    /**
     * @OneToMany to indicate that you can have one type of ingredient
     * with many ingredient_detail
     * 
     * i.e. You can have an Onion as an ingredient
     * and have ingredient_detail as - 1g chopped, 2g minced
     */
    @OneToMany(mappedBy = "ingredient")
    @JsonIgnore
    private Set<IngredientDetail> ingredientDetails;
}
