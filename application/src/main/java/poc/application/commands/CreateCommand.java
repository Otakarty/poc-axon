package poc.application.commands;

import org.axonframework.commandhandling.model.Aggregate;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.commandhandling.model.AggregateNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import poc.domain.AggregateId;

public abstract class CreateCommand<ID extends AggregateId<?, ID>, T extends poc.domain.Aggregate<ID>>
    extends Command<ID, T> {
    private static final long serialVersionUID = 3456485453907321523L;
    transient Logger logger = LoggerFactory.getLogger(this.getClass());

    private final T aggregate;
    private Boolean generateWhiteEvent = false;

    public CreateCommand(final CommandInfo commandInfo, final T aggregate) {
        super(commandInfo, aggregate.getId());
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
            this.logger.error(e.getMessage());
            throw this.exceptionToThrow(e.getMessage(), e);
        }
    }

}
