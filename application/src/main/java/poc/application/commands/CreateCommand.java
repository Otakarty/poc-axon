package poc.application.commands;

import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.commandhandling.model.AggregateNotFoundException;
import org.axonframework.commandhandling.model.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import poc.domain.person.UID;

public abstract class CreateCommand<T> extends Command<T> {
    private final T aggregate;

    public CreateCommand(final OrderInfo originOrder, final UID id, final T aggregate) {
        super(originOrder, id);
        this.aggregate = aggregate;
    }

    @Autowired
    private Repository<T> axonRepository;

    @Override
    public void apply() throws CommandExecutionException {
        try {
            this.loadAggregate();
        } catch (AggregateNotFoundException e) {
            throw this.exceptionToThrow(e.getMessage());
        }
    }

    @Override
    public void applyToEventStore() throws Exception {
        // Check user not already existing
        try {
            this.axonRepository.load(this.getId().getValue());
            throw this.exceptionToThrow("Aggregate already exists");
        } catch (AggregateNotFoundException e) {
            // OK
        }

        this.axonRepository.newInstance(() -> {
            AggregateLifecycle.apply(this.getDomainEvent());
            return this.aggregate;
        });
    }

}
