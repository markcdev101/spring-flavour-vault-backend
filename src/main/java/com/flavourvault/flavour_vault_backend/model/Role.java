package com.flavourvault.flavour_vault_backend.model;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "roles")
@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Role {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(nullable=false)
	private Integer id;
	
	@Column(unique=true, nullable=false)
	@Enumerated(EnumType.STRING)
	private RoleEnum name;
	
	@Column(nullable=false)
	private String description;
	
	@CreationTimestamp
	@Column(updatable=false, name="created_at")
	private Date creatredAt;
	
	@UpdateTimestamp
	@Column(name="updated_at")
	private Date updatedAt;
	
	
	
	
	
	
	
}
