package poc.application.person.commands.exceptions;

import java.text.MessageFormat;

import org.axonframework.commandhandling.CommandExecutionException;

import poc.domain.person.UID;

public class CannotCreatePersonException extends CommandExecutionException {

    private static final long serialVersionUID = 3438817549472277168L;

    public CannotCreatePersonException(final UID uid, final String message) {
        super(MessageFormat.format("Cannot create person with uid {0}, cause: {1}", uid.getValue(), message),
            null);
    }
}
