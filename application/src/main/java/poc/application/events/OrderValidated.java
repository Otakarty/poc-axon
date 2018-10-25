package poc.application.events;

import java.util.List;

import poc.application.commands.Command;
import poc.domain.person.UID;

public final class OrderValidated {
    private final UID id;
    private final List<Command<?>> commandsToApply;

    public OrderValidated(final UID id, final List<Command<?>> commandsToApply) {
        this.id = id;
        this.commandsToApply = commandsToApply;
    }

    public final List<Command<?>> getCommandsToApply() {
        return this.commandsToApply;
    }

    public UID getId() {
        return this.id;
    }

}
