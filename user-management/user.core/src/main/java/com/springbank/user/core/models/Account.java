package com.springbank.user.core.models;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {
	
	@Size(min = 2, message="${account.username.length}")
	private String username;
	@Size(min = 7, message="${account.password.length}")
	private String password;
	@NotNull(message = "${account.role.null}")
	private List<Role> roles;
}
