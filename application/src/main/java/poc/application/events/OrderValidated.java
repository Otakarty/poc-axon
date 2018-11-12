package poc.application.events;

import java.util.List;
import java.util.UUID;

import poc.application.commands.Command;

public final class OrderValidated {
    private final UUID id;
    private final List<Command<?>> commandsToApply;

    public OrderValidated(final UUID id, final List<Command<?>> commandsToApply) {
        this.id = id;
        this.commandsToApply = commandsToApply;
    }

    public final List<Command<?>> getCommandsToApply() {
        return this.commandsToApply;
    }

    public UUID getId() {
        return this.id;
    }

}
