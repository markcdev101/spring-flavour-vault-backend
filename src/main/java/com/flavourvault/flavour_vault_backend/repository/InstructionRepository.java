package com.flavourvault.flavour_vault_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.flavourvault.flavour_vault_backend.model.Instruction;

@Repository
public interface InstructionRepository extends JpaRepository<Instruction, Long> {
}
