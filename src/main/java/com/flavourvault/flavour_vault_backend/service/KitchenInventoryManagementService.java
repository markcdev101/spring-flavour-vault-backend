package com.flavourvault.flavour_vault_backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flavourvault.flavour_vault_backend.entities.Kitchen_Inventory;
import com.flavourvault.flavour_vault_backend.repository.KitchenInventoryRepository;

@Service
public class KitchenInventoryManagementService {
	
	@Autowired
	private KitchenInventoryRepository kitchenInventoryRepository;
	
	// Fetch all kitchen inventory items
    public List<Kitchen_Inventory> getAllInventoryItems() {
        return kitchenInventoryRepository.findAll();
    }
	
	
	// Add new kitchen inventory item
	public Kitchen_Inventory addInventory(Kitchen_Inventory newInventory) {
	    return kitchenInventoryRepository.save(newInventory);
	}
	
	// Fetching kitchen inventory item by barcode
	public Optional<Kitchen_Inventory> getInventoryByBarcode(String barcode) {
	    return kitchenInventoryRepository.findByBarcode(barcode);
	}

	// Fetching kitchen inventory items by name
	public List<Kitchen_Inventory> getInventoryByName(String name) {
	    return kitchenInventoryRepository.findByNameIgnoreCase(name);
	}
	
	// Updating a kitchen inventory item
    public Kitchen_Inventory updateInventory(Long id, Kitchen_Inventory updatedInventory) {
        Optional<Kitchen_Inventory> existingInventoryOptional = kitchenInventoryRepository.findById(id);

        if (existingInventoryOptional.isPresent()) {
            Kitchen_Inventory existingInventory = existingInventoryOptional.get();
            existingInventory.setBrandName(updatedInventory.getBrandName());
            existingInventory.setName(updatedInventory.getName());
            existingInventory.setBarcode(updatedInventory.getBarcode());
            existingInventory.setLocation(updatedInventory.getLocation());
            existingInventory.setQuatity(updatedInventory.getQuatity());
            existingInventory.setExpirationDate(updatedInventory.getExpirationDate());
            existingInventory.setBestBeforeDate(updatedInventory.getBestBeforeDate());
            existingInventory.setPrice(updatedInventory.getPrice());
            return kitchenInventoryRepository.save(existingInventory);
        } else {
            throw new RuntimeException("Kitchen inventory item not found with id: " + id);
        }
    }

    // Deleting a kitchen inventory item by ID
    public void deleteInventory(Long id) {
        if (kitchenInventoryRepository.existsById(id)) {
            kitchenInventoryRepository.deleteById(id);
        } else {
            throw new RuntimeException("Kitchen inventory item not found with id: " + id);
        }
    }
}
