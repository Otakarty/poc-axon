package poc.application.commands;

import java.util.List;
import java.util.UUID;

import org.springframework.util.Assert;

public final class Order {
    private OrderInfo info;
    // TODO: generic id instead
    // TODO : remove on move @TargetAggregateIdentifier on order id
    // TODO: ? extends generic aggregate
    private List<Command<?>> commands;

    private Class<?> aggregateType;

    public Order() {
    }

    public Order(final OrderInfo info, final List<Command<?>> commands) {
        this.info = info;
        Assert.isTrue(!commands.isEmpty(), "Should contain at least one command");
        this.commands = commands;
        this.aggregateType = commands.get(0).getAggregateType();
    }

    public final OrderInfo getInfo() {
        return this.info;
    }

    public final List<Command<?>> getCommands() {
        return this.commands;
    }

    public final UUID getId() {
        return this.getInfo().getId();
    }

    public final Class<?> getAggregateType() {
        return this.aggregateType;
    }

}
