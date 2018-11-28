package poc.application.commands;

import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.commandhandling.model.AggregateNotFoundException;

import poc.domain.AggregateId;
import poc.domain.person.events.WhiteEventException;

public abstract class UpdateCommand<I, T> extends Command<I, T> {

    private static final long serialVersionUID = -4334780840288000812L;

    public UpdateCommand(final OrderInfo originOrder, final AggregateId<?, I> id) {
        super(originOrder, id);
    }

    public abstract void verify(T aggregate) throws WhiteEventException;

    @Override
    public void verify() {
        try {
            this.loadAggregate().execute(person -> {
                try {
                    this.verify(person);
                } catch (WhiteEventException e) {
                    this.generateWhiteEvent();
                }
            });
        } catch (AggregateNotFoundException | IllegalArgumentException e) {
            throw this.exceptionToThrow(e.getMessage(), e);
        }
    }

    @Override
    public void applyToEventStore() {
        if (this.generateWhiteEvent) {
            return;
        }

        try {
            this.loadAggregate().execute(person -> AggregateLifecycle.apply(this.getDomainEvent()));
        } catch (Exception e) {
            // If white event, nothing to do
            if (!(e.getCause() instanceof WhiteEventException)) {
                throw new CommandExecutionException(e.getCause().getMessage(), e.getCause());
            }
        }
    }
}