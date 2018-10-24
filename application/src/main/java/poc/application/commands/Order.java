package poc.application.commands;

import java.util.List;

import org.axonframework.commandhandling.TargetAggregateIdentifier;
import org.springframework.util.Assert;

import poc.domain.person.UID;

public final class Order {
    private final OrderInfo info;
    // TODO: generic id instead
    @TargetAggregateIdentifier
    private final UID id;
    private final List<Command> commands;

    public Order(final OrderInfo info, final List<Command> commands, final UID id) {
        this.info = info;
        Assert.isTrue(!commands.isEmpty(), "Should contain at least one command");
        this.commands = commands;
        this.id = id;
    }

    public final OrderInfo getInfo() {
        return this.info;
    }

    public final List<Command> getCommands() {
        return this.commands;
    }

    public final UID getId() {
        return this.id;
    }

}
