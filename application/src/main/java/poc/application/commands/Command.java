package poc.application.commands;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.UUID;

import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.TargetAggregateIdentifier;
import org.axonframework.commandhandling.model.Aggregate;
import org.springframework.util.Assert;

import poc.domain.AggregateId;
import poc.domain.events.DomainEvent;

public abstract class Command<ID extends AggregateId<?, ID>, T extends poc.domain.Aggregate<ID>>
    implements Serializable {
    private static final long serialVersionUID = -8528846602698939210L;
    protected final UUID commandId;
    protected final OrderInfo originOrder;
    @TargetAggregateIdentifier
    protected final AggregateId<?, ID> aggregateId;

    protected Boolean generateWhiteEvent;

    public Command(final OrderInfo originOrder, final AggregateId<?, ID> id) {
        Assert.isTrue(id != null, "In order to create command, aggregate id should not be null");
        this.originOrder = originOrder;
        this.commandId = UUID.randomUUID();
        this.aggregateId = id;
        this.generateWhiteEvent = false;
    }

    public OrderInfo getOriginOrder() {
        return this.originOrder;
    }

    public final ID getAggregateId() {
        return (ID) this.aggregateId;
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

    /**
     * Get aggregate type class
     * @return T aggregate type class
     */
    @SuppressWarnings("unchecked")
    public Class<T> getAggregateType() {
        return (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }
}
