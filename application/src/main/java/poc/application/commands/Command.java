package poc.application.commands;

import java.io.Serializable;
import java.util.UUID;

import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.TargetAggregateIdentifier;
import org.axonframework.commandhandling.model.Aggregate;
import org.springframework.core.GenericTypeResolver;
import org.springframework.util.Assert;

import poc.domain.events.DomainEvent;
import poc.domain.person.UID;

public abstract class Command<T> implements Serializable {
    private static final long serialVersionUID = -8528846602698939210L;
    protected final UUID commandId;
    protected final OrderInfo originOrder;
    @TargetAggregateIdentifier
    protected final UID aggregateId;

    public Command(final OrderInfo originOrder, final UID id) {
        Assert.isTrue(id != null, "In order to create command, aggregate id should not be null");
        this.originOrder = originOrder;
        this.commandId = UUID.randomUUID();
        this.aggregateId = id;
    }

    public OrderInfo getOriginOrder() {
        return this.originOrder;
    }

    public final UID getAggregateId() {
        return this.aggregateId;
    }

    public final UUID getCommandId() {
        return this.commandId;
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

    // /**
    // * Get command args (to store into database)
    // * @return command args
    // */
    // protected abstract Map<String, Object> getCommandArgs();

    protected Aggregate<T> loadAggregate() {
        return Registry.getRepository(this.getAggregateType()).load(this.getAggregateId().getValue());
    }

    public Class<T> getAggregateType() {
        return (Class<T>) GenericTypeResolver.resolveTypeArgument(this.getClass(), Command.class);
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
