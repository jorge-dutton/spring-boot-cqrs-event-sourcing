package com.springbank.user.cmd.api.aggregates;

import java.util.UUID;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import com.springbank.user.cmd.api.commands.RegisterUserCommand;
import com.springbank.user.cmd.api.commands.RemoveUserCommand;
import com.springbank.user.cmd.api.commands.UpdateUserCommand;
import com.springbank.user.cmd.api.security.PasswordEncoder;
import com.springbank.user.cmd.api.security.PasswordEncoderImpl;
import com.springbank.user.core.events.UserRegisteredEvent;
import com.springbank.user.core.events.UserRemovedEvent;
import com.springbank.user.core.events.UserUpdatedEvent;
import com.springbank.user.core.models.User;

/**
 * The aggregate class is where the commands will be handled and where the
 * events, in response to the command handling, will be raised, once the
 * commands have been actioned.
 * 
 * For the Axon framework an aggregate class must be always annotated with
 * the @Aggregate annotation.
 * 
 * It must contain an id field annotated with the @AggregateIdentifier
 * annotation
 * 
 * Also, it must define two constructors:
 * 
 * One without arguments and the others that receives a Command arguments, the
 * latter must be annotated as @CommandHandlet.
 * 
 * Then, we will create several handle methods, annotated with
 * the @CommandHandler annotation. The idea is to have as many handle methods
 * with command arguments, as operations we have defined.
 * 
 * Then we must define Event Sourcing Handle methods for each handle we've
 * created plus the constructor with the argument.
 * 
 * @author jorge.dutton
 *
 */

@Aggregate
public class UserAggregate {

	// this annotation marks the field as the reference point
	// to the aggregate object that uses axon, to know which
	// aggregate the given command is targeting.
	@AggregateIdentifier
	private String id;
	private User user;

	private final PasswordEncoder passwordEncoder;

	public UserAggregate() {
		super();
		this.passwordEncoder = new PasswordEncoderImpl();
	}

	@CommandHandler
	public UserAggregate(RegisterUserCommand command) {
		
		var newUser = command.getUser();
		newUser.setId(command.getId());

		var password = newUser.getAccount().getPassword();
		this.passwordEncoder = new PasswordEncoderImpl();
		var hashedPassword = this.passwordEncoder.hashPassword(password);

		newUser.getAccount().setPassword(hashedPassword);

		// Once we have stored the user registration data
		// we raise an event to the command bus
		var event = UserRegisteredEvent.builder().id(command.getId()).user(newUser).build();
		
		// The apply method persist the event in the event store 
		// and sends it to the command bus
		AggregateLifecycle.apply(event);
	}

	@CommandHandler
	public void handle(UpdateUserCommand command) {
		
		var updateUser = command.getUser();
		updateUser.setId(command.getId());
		
		var password = updateUser.getAccount().getPassword();
		var hashedPassword = this.passwordEncoder.hashPassword(password);
		updateUser.getAccount().setPassword(hashedPassword);
		
		// We must set the event id to a random UUID and not the id from the command
		// Each event should have a unique id and not the same as the UserRegisteredEvent
		// used upon the first creation of the user
		var event = UserRegisteredEvent.builder().id(UUID.randomUUID().toString()).user(updateUser).build();
		
		AggregateLifecycle.apply(event);
	}

	@CommandHandler
	public void handle(RemoveUserCommand command) {
		// For the remove user we simply instantiate a new UserRemovedEvent
		// set its id from the command id
		var event = new UserRemovedEvent();
		event.setId(command.getId());
		
		// Finally invoke apply to store event and send to command bus
		AggregateLifecycle.apply(event);
	}

	@EventSourcingHandler
	public void on(UserRegisteredEvent event) {
		// here we set the AggregateIdentifier field to the value set on the event
		// which comes all the way from the command.
		// And the user that also comes from the command, via the event
		this.id = event.getId();
		this.user = event.getUser();
	}

	@EventSourcingHandler
	public void on(UserUpdatedEvent event) {
		// This time we only update the user info
		// contained into the event, since the the correct
		// aggregate instance was selected when the updateUserCommand
		// was handled.
		// This is when the field annotated with @TargetAggregateIdentifier
		// from the UpdateUserCommand, comes into play
		this.user = event.getUser();

	}

	@EventSourcingHandler
	public void on(UserRemovedEvent event) {
		// For remove the user, we simply invoke markDeleted
		// to mark the actual aggregate for deletion
		AggregateLifecycle.markDeleted();
	}
}
