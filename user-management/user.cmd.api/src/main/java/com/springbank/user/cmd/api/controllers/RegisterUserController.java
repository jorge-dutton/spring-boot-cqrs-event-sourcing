package com.springbank.user.cmd.api.controllers;

import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springbank.user.cmd.api.commands.RegisterUserCommand;
import com.springbank.user.cmd.api.dto.RegisterUserResponse;

@RestController
@RequestMapping(path = "/api/v1/registerUser")
public class RegisterUserController {

	private static final Logger LOG = LoggerFactory.getLogger(RegisterUserController.class);

	// The command gateway is an Axon mecanism
	// that will dispatch the RegisterUserCommand.
	// Once dispatched it will invoke the command
	// handler constructor on the RegisteredUserCommand
	// and the command handling methods on other user commands
	private final CommandGateway commandGateway;

	public RegisterUserController(CommandGateway commandGateway) {
		super();
		this.commandGateway = commandGateway;
	}

	@PostMapping
	public ResponseEntity<RegisterUserResponse> registerUser(@RequestBody RegisterUserCommand command) {

		command.setId(UUID.randomUUID().toString());

		try {
			// Send the command and wait for any response.
			commandGateway.sendAndWait(command);
			
			return new ResponseEntity<>(new RegisterUserResponse("User successfully registered"), HttpStatus.OK);
		} catch (Exception e) {
			var safeErrorMessage = String.format("Error while processing register user request for id %s",
					command.getId());
			LOG.error(e.getMessage());

			return new ResponseEntity<>(new RegisterUserResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
