package com.flavourvault.flavour_vault_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flavourvault.flavour_vault_backend.entities.Kitchen_Inventory;

@Repository
public interface KitchenInventoryRepository extends JpaRepository<Kitchen_Inventory, Long> {

}
