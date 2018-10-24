package poc.application.commands;

import static org.axonframework.eventhandling.GenericEventMessage.asEventMessage;

import java.util.ArrayList;
import java.util.List;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.Aggregate;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.commandhandling.model.AggregateNotFoundException;
import org.axonframework.commandhandling.model.Repository;
import org.axonframework.eventhandling.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import poc.application.events.InvalidOrderException;
import poc.application.events.OrderValidated;
import poc.application.person.commands.ChangePersonName;
import poc.domain.events.DomainEvent;
import poc.domain.person.Person;
import poc.domain.person.events.PersonNameChanged;

@Component
public class OrderHandler {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Repository<Person> axonRepo;

    @Autowired
    private EventBus eventBus;

    @CommandHandler
    public void handle(final Order order) {
        this.logger.info("Handling Order command");
        try {
            Aggregate<Person> personAggregate = this.axonRepo.load(order.getId().getValue());
            List<DomainEvent> domainEventsToApply = new ArrayList<>();

            personAggregate.execute(person -> {
                order.getCommands().stream().forEach(command -> {
                    String commandName = command.getCommandName();
                    switch (commandName) {
                    case "ChangePersonName":
                        // TODO: Refactor
                        ChangePersonName castedCommand = (ChangePersonName) command;
                        person.changeName(castedCommand.getName());
                        domainEventsToApply
                            .add(new PersonNameChanged(castedCommand.getUid(), castedCommand.getName()));
                        break;
                    default:
                        throw new UnsupportedOperationException("Not yet implemented");
                    }
                });
                AggregateLifecycle.apply(new OrderValidated(order.getId(), domainEventsToApply));
            });
        } catch (IllegalArgumentException | AggregateNotFoundException e) {
            InvalidOrderException ex = new InvalidOrderException(order.getId(), e.getMessage());
            this.eventBus.publish(asEventMessage(ex));
            this.logger.error(ex.getMessage());
        }
    }
}
