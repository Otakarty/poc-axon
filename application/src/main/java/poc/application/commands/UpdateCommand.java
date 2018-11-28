package poc.application.commands;

import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.commandhandling.model.AggregateNotFoundException;

import poc.domain.AggregateId;
import poc.domain.person.events.WhiteEventException;

public abstract class UpdateCommand<ID extends AggregateId<?, ID>, T extends poc.domain.Aggregate<ID>>
    extends Command<ID, T> {

    private static final long serialVersionUID = -4334780840288000812L;

    public UpdateCommand(final CommandInfo originOrder, final ID id) {
        super(originOrder, id);
    }

    @Override
    public void apply() {
        try {
            this.loadAggregate().execute(person -> AggregateLifecycle.apply(this.getDomainEvent()));
        } catch (AggregateNotFoundException | IllegalArgumentException e) {
            throw this.exceptionToThrow(e.getMessage(), e);
        } catch (Exception e) {
            // If white event, nothing to do
            if (!(e.getCause() instanceof WhiteEventException)) {
                throw this.exceptionToThrow(e.getCause().getMessage(), e.getCause());
            }
        }
    }
}