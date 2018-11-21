package poc.application.commands;

import org.axonframework.commandhandling.model.Aggregate;
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
    public void verify() {
        // Check user not already existing
        try {
            Aggregate<T> aggregate = this.loadAggregate();
            aggregate.execute(root -> {
                if (!root.equals(this.aggregate)) {
                    throw this.exceptionToThrow("Aggregate already exists", null);
                } else {
                    this.generateWhiteEvent();
                }
            });
        } catch (AggregateNotFoundException e) {
            // OK
        }
    }

    @Override
    public void applyToEventStore() {
        if (this.generateWhiteEvent) {
            return;
        }

        try {
            Registry.getRepository(this.getAggregateType()).newInstance(() -> {
                AggregateLifecycle.apply(this.getDomainEvent());
                return this.aggregate;
            });
        } catch (Exception e) {
            throw this.exceptionToThrow(e.getMessage(), e);
        }
    }

}
