package com.springbank.user.cmd.api.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.springbank.user.core.models.User;

import lombok.Builder;
import lombok.Data;

/**
 * 
 * @author jorge.dutton
 *
 */

@Data
@Builder
public class RegisterUserCommand {
	// In order for Axon to know which instance of an aggregate should
	// handle the command message, the field carrying the aggregate
	// identifier in the command object must be annotated with
	// the @TargetAggregateIdentifier
	@TargetAggregateIdentifier
	private String id;
	
	// The command should carry the information required to undertake
	// the action based on the expressed intent (Register User)
	private User user;
}
