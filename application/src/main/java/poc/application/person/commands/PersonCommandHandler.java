package poc.application.person.commands;

import static org.axonframework.eventhandling.GenericEventMessage.asEventMessage;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.Aggregate;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.commandhandling.model.AggregateNotFoundException;
import org.axonframework.commandhandling.model.Repository;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.messaging.annotation.MessageHandlerInvocationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import poc.application.person.commands.exceptions.CannotChangeNameException;
import poc.application.person.commands.exceptions.CannotCreatePersonException;
import poc.domain.person.Person;
import poc.domain.person.events.PersonCreated;
import poc.domain.person.events.PersonNameChanged;

@Component
public class PersonCommandHandler {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Repository<Person> axonRepo;

    @Autowired
    private EventBus eventBus;

    // @Autowired
    // private Persons repository;

    @CommandHandler
    public void handle(final CreatePerson command) throws Exception {
        this.logger.info("Handling CreatePerson command");

        // Check user not already existing
        try {
            this.axonRepo.load(command.getUid().getValue());
            CannotCreatePersonException e =
                new CannotCreatePersonException(command.getUid(), "person already exists", null);
            this.eventBus.publish(asEventMessage(e));
            this.logger.error(e.getMessage());
            return;
            // throw e;
        } catch (AggregateNotFoundException e) {
            // OK
        }

        this.axonRepo.newInstance(() -> {
            AggregateLifecycle.apply(new PersonCreated(command.getCommandId(), command.getPerson()));
            return command.getPerson();
        });
    }

    @CommandHandler
    public void handle(final ChangePersonName command) {
        this.logger.info("Handling ChangePersonName command");
        try {
            Aggregate<Person> personAggregate = this.axonRepo.load(command.getAggregateId().getValue());

            personAggregate.execute(person -> AggregateLifecycle.apply(
                new PersonNameChanged(command.getCommandId(), command.getAggregateId(), command.getName())));
        } catch (AggregateNotFoundException e) {
            CannotChangeNameException ex =
                new CannotChangeNameException(command.getAggregateId(), command.getName(), e.getMessage(), e);
            this.eventBus.publish(asEventMessage(ex));
            this.logger.error(ex.getMessage());
        } catch (MessageHandlerInvocationException e) {
            CannotChangeNameException ex = new CannotChangeNameException(command.getAggregateId(),
                command.getName(), e.getCause().getMessage(), e);
            this.eventBus.publish(asEventMessage(ex));
            this.logger.error(ex.getMessage());
        }
    }
}
