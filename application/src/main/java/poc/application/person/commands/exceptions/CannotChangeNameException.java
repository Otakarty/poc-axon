package poc.application.person.commands.exceptions;

import java.text.MessageFormat;

import org.axonframework.commandhandling.CommandExecutionException;

import poc.domain.person.UID;

public class CannotChangeNameException extends CommandExecutionException {

    private static final long serialVersionUID = 1212145618029004357L;

    public CannotChangeNameException(final UID uid, final String message) {
        super(MessageFormat.format("Cannot rename person with uid {0}, cause: {1}", uid.getValue(), message),
            null);
    }

}
