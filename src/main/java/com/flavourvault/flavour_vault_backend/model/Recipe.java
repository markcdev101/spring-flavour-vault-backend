package com.flavourvault.flavour_vault_backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Recipe {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String description;
	private String ingredients;
	private String instructions;
	

}
