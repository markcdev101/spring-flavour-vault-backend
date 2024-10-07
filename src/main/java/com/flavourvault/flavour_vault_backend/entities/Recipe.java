package com.flavourvault.flavour_vault_backend.entities;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Entity - to indicate that this is a JPA entity
 * @Table - 
 * @Getter - using Project Lombok to auto generate the getters for the fields
 * @Setter - using Project Lombok to auto generate the setters for the fields
 * @NoArgsConstructor - using Project Lombok to generate a constructor without argument.
 */
@Entity
@Table(name = "recipes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Recipe implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1704498941193875915L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String description;

	private int preparationTime; 
    private int cookingTime; 
    
    
    /**
     * @OneToMany to indicate that you can have one recipe but many ingredient_detail
     * 
     * i.e. You have 1g chopped onion and also 2g minced onion in one recipe.
     * i.e. You have 20g chopped oreos and also 5 whole oreos in one recipe.
     */
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // This side will be serialized
    private List<IngredientDetail> ingredientDetails;

    
    /**
     * @OneToMany to indicate that you can have one recipe have many instructions
     * 
     * i.e. In one recipe you can have 10 steps/instructions on how to cooked a cheesecake.
     */
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference 
    private List<Instruction> instructions;
	
    

}
