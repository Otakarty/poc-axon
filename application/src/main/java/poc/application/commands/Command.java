package poc.application.commands;

import java.io.Serializable;
import java.util.UUID;

import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.TargetAggregateIdentifier;
import org.axonframework.commandhandling.model.Aggregate;
import org.springframework.core.GenericTypeResolver;
import org.springframework.util.Assert;

import poc.domain.AggregateId;
import poc.domain.events.DomainEvent;

public abstract class Command<I, T> implements Serializable {
    private static final long serialVersionUID = -8528846602698939210L;
    protected final UUID commandId;
    protected final OrderInfo originOrder;
    @TargetAggregateIdentifier
    protected final AggregateId<?, I> aggregateId;

    protected Boolean generateWhiteEvent;

    public Command(final OrderInfo originOrder, final AggregateId<?, I> id) {
        Assert.isTrue(id != null, "In order to create command, aggregate id should not be null");
        this.originOrder = originOrder;
        this.commandId = UUID.randomUUID();
        this.aggregateId = id;
        this.generateWhiteEvent = false;
    }

    public OrderInfo getOriginOrder() {
        return this.originOrder;
    }

    public final AggregateId<?, I> getAggregateId() {
        return this.aggregateId;
    }

    public final UUID getCommandId() {
        return this.commandId;
    }

    public String getCommandName() {
        return this.getClass().getSimpleName();
    }

    /**
     * Apply command to event store
     * @throws Exception
     */
    public abstract void applyToEventStore() throws CommandExecutionException;

    /**
     * Verify command can be applied.
     */
    public abstract void verify();

    /**
     * The exception to throw if command fails.
     * @param causeMessage cause exception message
     * @return the exception
     */
    protected abstract CommandExecutionException exceptionToThrow(final String causeMessage,
        final Throwable cause);

    /**
     * Domain event to create if command succeeds.
     * @return domain event
     */
    protected abstract DomainEvent getDomainEvent();

    protected Aggregate<T> loadAggregate() {
        return Registry.getRepository(this.getAggregateType()).load(this.getAggregateId().toString());
    }

    protected void generateWhiteEvent() {
        this.generateWhiteEvent = true;
    }

    public Class<T> getAggregateType() {
        return (Class<T>) GenericTypeResolver.resolveTypeArgument(this.getClass(), Command.class);
    }
}
