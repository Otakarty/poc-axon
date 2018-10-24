package poc.application.events;

import java.text.MessageFormat;

import org.axonframework.commandhandling.CommandExecutionException;

import poc.domain.person.UID;

public class InvalidOrderException extends CommandExecutionException {

    private static final long serialVersionUID = 1212145618029004357L;

    public InvalidOrderException(final UID uid, final String message) {
        super(MessageFormat.format("Invalid order for uid {0}, cause: {1}", uid.getValue(), message), null);
    }
}
