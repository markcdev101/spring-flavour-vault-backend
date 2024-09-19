package com.flavourvault.flavour_vault_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "instructions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Instruction {

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
    private Recipe recipe;
}
