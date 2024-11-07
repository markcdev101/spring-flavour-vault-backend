package com.flavourvault.flavour_vault_backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "kitchen_inventory")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Kitchen_Inventory implements Serializable {



	/**
	 * 
	 */
	private static final long serialVersionUID = 9058485438882555190L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private String brandName;

    private String name;

    private String barcode;
    
    private String location;
    
    private int quatity;
    
    private Date expirationDate;
    
    private Date bestBeforeDate;
    
    private double price;
    

}
