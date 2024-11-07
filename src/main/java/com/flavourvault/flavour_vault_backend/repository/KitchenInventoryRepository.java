package com.flavourvault.flavour_vault_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flavourvault.flavour_vault_backend.entities.Kitchen_Inventory;

@Repository
public interface KitchenInventoryRepository extends JpaRepository<Kitchen_Inventory, Long> {
	// Custom query method to find by barcode
    Optional<Kitchen_Inventory> findByBarcode(String barcode);

    // Custom query method to find by name (case-insensitive)
    List<Kitchen_Inventory> findByNameIgnoreCase(String name);
}
