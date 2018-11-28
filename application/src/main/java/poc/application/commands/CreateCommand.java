package poc.application.commands;

import org.axonframework.commandhandling.model.Aggregate;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.commandhandling.model.AggregateNotFoundException;

import poc.domain.AggregateId;

public abstract class CreateCommand<I, T> extends Command<I, T> {
    private static final long serialVersionUID = 3456485453907321523L;
    private final T aggregate;

    public CreateCommand(final OrderInfo originOrder, final AggregateId<?, I> id, final T aggregate) {
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
