package com.springbank.user.core.events;

import com.springbank.user.core.models.User;

import lombok.Builder;
import lombok.Data;

/**
 * An event is something that happen when a command is executed.
 * 
 * Events are raised in the command API and are handled in the query API.
 * 
 * The UserRegisteredEvent is going to look the same as the RegisterUserCommand, except for the
 * @TargetAggregateIdentifier annotation
 * 
 * @author jorge.dutton
 *
 */

@Data
@Builder
public class UserRegisteredEvent {
	private String id;
	private User user;
}
