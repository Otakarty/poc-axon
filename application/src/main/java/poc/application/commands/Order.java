package poc.application.commands;

import java.util.List;

import org.axonframework.commandhandling.TargetAggregateIdentifier;
import org.springframework.util.Assert;

import poc.domain.person.UID;

public final class Order {
    private OrderInfo info;
    // TODO: generic id instead
    @TargetAggregateIdentifier
    private UID id;
    // TODO: ? extends generic aggregate
    private List<Command<?>> commands;

    private Class<?> aggregateType;

    public Order() {
    }

    public Order(final OrderInfo info, final List<Command<?>> commands, final UID id) {
        this.info = info;
        Assert.isTrue(!commands.isEmpty(), "Should contain at least one command");
        this.commands = commands;
        this.id = id;
        this.aggregateType = commands.get(0).getAggregateType();
    }

    public final OrderInfo getInfo() {
        return this.info;
    }

    public final List<Command<?>> getCommands() {
        return this.commands;
    }

    public final UID getId() {
        return this.id;
    }

    public final Class<?> getAggregateType() {
        return this.aggregateType;
    }

}
