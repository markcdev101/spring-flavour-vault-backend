package com.flavourvault.flavour_vault_backend.entities;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "instructions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Instruction  implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3061752058900869613L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String step;

    /**
     * @ManyToOne to indicate that you can have many instructions per 
     * recipe
     * 
     * i.e. In one recipe you can have 10 steps/instructions on how to cooked a cheesecake.
     */
    @ManyToOne
    @JoinColumn(name = "recipe_id")
    @JsonBackReference
    private Recipe recipe;
}
