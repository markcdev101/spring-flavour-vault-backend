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

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;
}
