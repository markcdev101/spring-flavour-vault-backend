package com.flavourvault.flavour_vault_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ProfileDto {
//	private Integer id;
    private String email;
    private String fullName;
//	private Profile profile; // Include only if profile information is needed
}