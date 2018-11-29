package poc.application.commands;

import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.commandhandling.model.AggregateNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import poc.domain.AggregateId;
import poc.domain.exceptions.WhiteEventException;

public abstract class UpdateCommand<ID extends AggregateId<?, ID>, T extends poc.domain.Aggregate<ID>>
    extends Command<ID, T> {
    private static final long serialVersionUID = -4334780840288000812L;
    transient Logger logger = LoggerFactory.getLogger(this.getClass());

    public UpdateCommand(final CommandInfo commandInfo, final ID id) {
        super(commandInfo, id);
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
                this.logger.error(e.getCause().getMessage());
                throw this.exceptionToThrow(e.getCause().getMessage(), e.getCause());
            } else {
                this.logger.info(e.getCause().getMessage());
            }
        }
    }
}