package com.flavourvault.flavour_vault_backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProfileNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3181420459554116307L;

	public ProfileNotFoundException(String email) {
		super("No profile found for email: " + email);
	}

}
