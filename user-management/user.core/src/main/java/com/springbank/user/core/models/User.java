package com.springbank.user.core.models;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "users")
public class User {

	@Id
	private String id; // BongoDB _id field for unique identifying a document
						// from a collection

	@NotEmpty(message = "{user.firstname.empty}")
	private String firstname;
	
	@NotEmpty(message = "{user.lastname.empty}")
	private String lastname;
	
	@NotEmpty(message = "{user.email.empty}")
	@Email
	private String email;
	
	@NotNull(message = "{user.account.null}")
	@Valid
	private Account account;

}
