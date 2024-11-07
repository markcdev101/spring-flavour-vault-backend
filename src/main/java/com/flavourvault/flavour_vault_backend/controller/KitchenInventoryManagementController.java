package com.flavourvault.flavour_vault_backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flavourvault.flavour_vault_backend.entities.Kitchen_Inventory;
import com.flavourvault.flavour_vault_backend.service.JwtService;
import com.flavourvault.flavour_vault_backend.service.KitchenInventoryManagementService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/flavourvault/api")
@AllArgsConstructor
public class KitchenInventoryManagementController {

	@Autowired
	private KitchenInventoryManagementService kitchenInventoryManagementService;

	@Autowired
	private final JwtService jwtService;


	// Fetch all kitchen inventory items
	@GetMapping("/inventory")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<List<Kitchen_Inventory>> getAllInventoryItems(
			@CookieValue(name = "jwtToken", required = false) String token) {
		if (token == null || !jwtService.isTokenValid(token)) {
			return ResponseEntity.status(401).body(null);
		}

		log.info("START GET /inventory endpoint");
		List<Kitchen_Inventory> inventoryItems = kitchenInventoryManagementService.getAllInventoryItems();
		log.info("END GET /inventory endpoint");

		return ResponseEntity.ok(inventoryItems);
	}


	// Fetch kitchen inventory item by barcode
	@GetMapping("/inventory/barcode/{barcode}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<Kitchen_Inventory> getInventoryByBarcode(
			@CookieValue(name = "jwtToken", required = false) String token,
			@PathVariable String barcode) {
		if (token == null || !jwtService.isTokenValid(token)) {
			return ResponseEntity.status(401).body(null);
		}

		log.info("START GET /inventory/barcode/{barcode} endpoint");
		Optional<Kitchen_Inventory> inventory = kitchenInventoryManagementService.getInventoryByBarcode(barcode);
		log.info("END GET /inventory/barcode/{barcode} endpoint");

		return inventory.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	// Fetch kitchen inventory items by name
	@GetMapping("/inventory/name/{name}")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<List<Kitchen_Inventory>> getInventoryByName(
			@CookieValue(name = "jwtToken", required = false) String token,
			@PathVariable String name) {
		if (token == null || !jwtService.isTokenValid(token)) {
			return ResponseEntity.status(401).body(null);
		}

		log.info("START GET /inventory/name/{name} endpoint");
		List<Kitchen_Inventory> inventoryList = kitchenInventoryManagementService.getInventoryByName(name);
		log.info("END GET /inventory/name/{name} endpoint");

		return ResponseEntity.ok(inventoryList);
	}

	// Add a new kitchen inventory item
	@PostMapping("/inventory")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Kitchen_Inventory> addInventory(
			@CookieValue(name = "jwtToken", required = false) String token,
			@RequestBody Kitchen_Inventory newInventory) {
		if (token == null || !jwtService.isTokenValid(token)) {
			return ResponseEntity.status(401).body(null);
		}

		log.info("START POST /inventory endpoint");
		Kitchen_Inventory createdInventory = kitchenInventoryManagementService.addInventory(newInventory);
		log.info("END POST /inventory endpoint");
		return ResponseEntity.ok(createdInventory);
	}

	// Update kitchen inventory item by ID
	@PutMapping("/inventory/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Kitchen_Inventory> updateInventory(
			@CookieValue(name = "jwtToken", required = false) String token,
			@PathVariable Long id,
			@RequestBody Kitchen_Inventory updatedInventory) {
		if (token == null || !jwtService.isTokenValid(token)) {
			return ResponseEntity.status(401).body(null);
		}

		log.info("START PUT /inventory/{id} endpoint");
		try {
			Kitchen_Inventory updated = kitchenInventoryManagementService.updateInventory(id, updatedInventory);
			log.info("END PUT /inventory/{id} endpoint");
			return ResponseEntity.ok(updated);
		} catch (RuntimeException e) {
			log.info("END PUT /inventory/{id} endpoint - Item not found");
			return ResponseEntity.notFound().build();
		}
	}

	// Delete kitchen inventory item by ID
	@DeleteMapping("/inventory/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Void> deleteInventory(
			@CookieValue(name = "jwtToken", required = false) String token,
			@PathVariable Long id) {
		if (token == null || !jwtService.isTokenValid(token)) {
			return ResponseEntity.status(401).body(null);
		}

		log.info("START DELETE /inventory/{id} endpoint");
		try {
			kitchenInventoryManagementService.deleteInventory(id);
			log.info("END DELETE /inventory/{id} endpoint");
			return ResponseEntity.noContent().build();
		} catch (RuntimeException e) {
			log.info("END DELETE /inventory/{id} endpoint - Item not found");
			return ResponseEntity.notFound().build();
		}
	}
}
