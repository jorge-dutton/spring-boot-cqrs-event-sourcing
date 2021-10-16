package com.springbank.user.core.models;

import org.springframework.security.core.GrantedAuthority;

/**
 * Implements GrantedAutority interface to comply with Spring Security roles
 * 
 * @author jorge.dutton
 *
 */
public enum Role implements GrantedAuthority {
	READ_PRIVILEGE, WRITE_PRIVILEGE;

	@Override
	public String getAuthority() {
		return name();
	}

}
