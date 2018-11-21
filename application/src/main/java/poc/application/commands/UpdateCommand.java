package poc.application.commands;

import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.commandhandling.model.AggregateNotFoundException;

import poc.domain.person.UID;

public abstract class UpdateCommand<T> extends Command<T> {

    public UpdateCommand(final OrderInfo originOrder, final UID id) {
        super(originOrder, id);
    }

    public abstract void apply(T aggregate) throws CommandExecutionException;

    @Override
    public void apply() throws CommandExecutionException {
        try {
            this.loadAggregate().execute(person -> this.apply(person));
        } catch (AggregateNotFoundException | IllegalArgumentException e) {
            throw this.exceptionToThrow(e.getMessage(), e);
        }
    }

    @Override
    public void applyToEventStore() {
        this.loadAggregate().execute(person -> AggregateLifecycle.apply(this.getDomainEvent()));
    }
}