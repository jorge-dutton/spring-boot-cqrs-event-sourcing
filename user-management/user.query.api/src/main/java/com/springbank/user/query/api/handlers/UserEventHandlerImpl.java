package com.springbank.user.query.api.handlers;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Service;

import com.springbank.user.core.events.UserRegisteredEvent;
import com.springbank.user.core.events.UserRemovedEvent;
import com.springbank.user.core.events.UserUpdatedEvent;
import com.springbank.user.query.api.repositories.UserRepository;

/**
 * A service is an DDD term to designate an operation offered as an
 * interface that stands alone in the model and has no encapsulated
 * state.
 * 
 * The processing group is similar to a consumer group, which means 
 * that, when an event handler consumes an event Axon will track
 * and offset to make sure that within a given processing group 
 * you will always consume the last event.
 * 
 * Axon manages the consumed offset separately for each processing group.
 * 
 * @author jorge.dutton
 *
 */

@Service
@ProcessingGroup("user-group")
public class UserEventHandlerImpl implements UserEventHandler {
	
	private final UserRepository userRepository;
	
	
	
	public UserEventHandlerImpl(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}
	
	/**
	 * The EventHandler annotation marks the function as an EventHandler.
	 * 
	 * Event Handlers, basically, define the business logic to be performed
	 * when an event is received or consumed from the event bus.
	 *  
	 */
	@EventHandler
	@Override
	public void on(UserRegisteredEvent event) {
		this.userRepository.save(event.getUser());
	}

	@EventHandler
	@Override
	public void on(UserUpdatedEvent event) {
		this.userRepository.save(event.getUser());
	}

	@EventHandler
	@Override
	public void on(UserRemovedEvent event) {
		this.userRepository.deleteById(event.getId());
	}

}
