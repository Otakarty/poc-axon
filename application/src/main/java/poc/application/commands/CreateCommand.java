package poc.application.commands;

import org.axonframework.commandhandling.model.Aggregate;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.commandhandling.model.AggregateNotFoundException;

import poc.domain.AggregateId;

public abstract class CreateCommand<ID extends AggregateId<?, ID>, T extends poc.domain.Aggregate<ID>>
    extends Command<ID, T> {
    private static final long serialVersionUID = 3456485453907321523L;
    private final T aggregate;
    private Boolean generateWhiteEvent;

    public CreateCommand(final CommandInfo originOrder, final T aggregate) {
        super(originOrder, aggregate.getId());
        this.aggregate = aggregate;
    }

    public void checkUserDoesNotExists() {
        // Check user not already existing
        try {
            Aggregate<T> aggregate = this.loadAggregate();
            aggregate.execute(root -> {
                if (!root.equals(this.aggregate)) {
                    throw this.exceptionToThrow("Different aggregate with same id already exists", null);
                } else {
                    this.generateWhiteEvent = true;
                }
            });
        } catch (AggregateNotFoundException e) {
            // OK
        }
    }

    @Override
    public void apply() {
        this.checkUserDoesNotExists();
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
