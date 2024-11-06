package com.flavourvault.flavour_vault_backend.dto;

import com.flavourvault.flavour_vault_backend.entities.Profile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UserInfoDto {
	private Integer id;
	private String username;
	private String role;
	private ProfileDto profile; 
}


