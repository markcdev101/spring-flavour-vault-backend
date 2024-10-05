package com.flavourvault.flavour_vault_backend.model;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * JPA Entity for User
 */
@Table(name = "users")
@Entity
@Getter @Setter @AllArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable=false)
	private Integer id;
	
	@Column(nullable=false)
	private String fullName;
	
	@Column(unique=true, length=100, nullable=false)
	private String email;
	
	@Column(nullable=false)
	private String password;
	
	@CreationTimestamp
	@Column(updatable = false, name="created_at")
	private Date createdAt;
	
	@UpdateTimestamp
	@Column(name="updated_at")
	private Date updatedAt;
	
}
