package poc.application.events.exceptions;

import java.text.MessageFormat;
import java.util.Map;
import java.util.UUID;

import org.axonframework.commandhandling.CommandExecutionException;
import org.springframework.data.util.Pair;

import poc.application.commands.Command;
import poc.application.commands.OrderInfo;

public class InvalidOrderException extends CommandExecutionException {

    private static final long serialVersionUID = 1212145618029004357L;

    private final OrderInfo origin;
    private final Map<UUID, Pair<Command<?>, CommandExecutionException>> inErrorCommands;

    public InvalidOrderException(final OrderInfo origin,
        final Map<UUID, Pair<Command<?>, CommandExecutionException>> inErrorCommands) {
        super(MessageFormat.format("Invalid order {0}, causes: {1}", origin.getId(), inErrorCommands), null);
        this.origin = origin;
        this.inErrorCommands = inErrorCommands;

    }

    public final OrderInfo getOrigin() {
        return this.origin;
    }

    public final Map<UUID, Pair<Command<?>, CommandExecutionException>> getInErrorCommands() {
        return this.inErrorCommands;
    }
}
