package com.springbank.user.query.api.handlers;

import com.springbank.user.core.events.UserRegisteredEvent;
import com.springbank.user.core.events.UserRemovedEvent;
import com.springbank.user.core.events.UserUpdatedEvent;

/**
 * Interface to implement classes for handling of several events
 * injected into the Axon event bus.
 * 
 * @author jorge.dutton
 *
 */
public interface UserEventHandler {
	void on(UserRegisteredEvent event);
	void on(UserUpdatedEvent event);
	void on(UserRemovedEvent event);

}
