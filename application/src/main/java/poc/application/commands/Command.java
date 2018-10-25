package poc.application.commands;

import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.TargetAggregateIdentifier;
import org.axonframework.commandhandling.model.Aggregate;
import org.springframework.core.GenericTypeResolver;
import org.springframework.util.Assert;

import poc.domain.events.DomainEvent;
import poc.domain.person.UID;

public abstract class Command<T> {
    private final OrderInfo originOrder;
    @TargetAggregateIdentifier
    private final UID id;

    public Command(final OrderInfo originOrder, final UID id) {
        Assert.isTrue(id != null, "In order to create command, aggregate id should not be null");
        this.originOrder = originOrder;
        this.id = id;
    }

    public OrderInfo getOriginOrder() {
        return this.originOrder;
    }

    public final UID getId() {
        return this.id;
    }

    public abstract String getCommandName();

    /**
     * Apply command to event store
     * @throws Exception
     */
    public abstract void applyToEventStore() throws Exception;

    /**
     * Apply command.
     */
    public abstract void apply();

    /**
     * The exception to throw if command fails.
     * @param causeMessage cause exception message
     * @return the exception
     */
    protected abstract CommandExecutionException exceptionToThrow(final String causeMessage);

    /**
     * Domain event to create if command succeeds.
     * @return domain event
     */
    protected abstract DomainEvent getDomainEvent();

    protected Aggregate<T> loadAggregate() {
        return Registry
            .getRepository((Class<T>) GenericTypeResolver.resolveTypeArgument(this.getClass(), Command.class))
            .load(this.getId().getValue());
    }

    /**
     * Apply command and return associated domain event.
     * @return resulting domain event if success
     */
    // TODO: change with checked exception extending CommandExecutionException to force handling
    public DomainEvent applyAndGetEvent() throws CommandExecutionException {
        this.apply();
        return this.getDomainEvent();
    }
}
