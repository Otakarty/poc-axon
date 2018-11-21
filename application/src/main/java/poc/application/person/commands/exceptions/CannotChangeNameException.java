package poc.application.person.commands.exceptions;

import java.text.MessageFormat;

import org.axonframework.commandhandling.CommandExecutionException;

import poc.domain.person.Name;
import poc.domain.person.UID;

public class CannotChangeNameException extends CommandExecutionException {

    private static final long serialVersionUID = 1212145618029004357L;

    public CannotChangeNameException(final UID uid, final Name name, final String message, final Throwable cause) {
        super(MessageFormat.format("Cannot change person name with uid {0} into {1}, cause: {2}", uid.getValue(),
            name.getValue(), message), cause);
    }

}
