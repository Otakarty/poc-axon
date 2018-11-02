package poc.application.commands;

import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.commandhandling.model.AggregateNotFoundException;

import poc.domain.person.UID;

public abstract class CreateCommand<T> extends Command<T> {
    private final T aggregate;

    public CreateCommand(final OrderInfo originOrder, final UID id, final T aggregate) {
        super(originOrder, id);
        this.aggregate = aggregate;
    }

    @Override
    public void apply() throws CommandExecutionException {
        // Check user not already existing
        try {
            this.loadAggregate();
            throw this.exceptionToThrow("Aggregate already exists");
        } catch (AggregateNotFoundException e) {
            // OK
        }
    }

    @Override
    public void applyToEventStore() throws Exception {
        // Check user not already existing
        try {
            Registry.getRepository(this.getAggregateType()).load(this.getAggregateId().getValue());
            throw this.exceptionToThrow("Aggregate already exists");
        } catch (AggregateNotFoundException e) {
            // OK
        }

        Registry.getRepository(this.getAggregateType()).newInstance(() -> {
            AggregateLifecycle.apply(this.getDomainEvent());
            return this.aggregate;
        });
    }

}
