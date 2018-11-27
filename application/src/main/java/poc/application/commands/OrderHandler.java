package poc.application.commands;

import static org.axonframework.eventhandling.GenericEventMessage.asEventMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import poc.application.events.OrderValidated;
import poc.application.events.exceptions.InvalidOrderException;

@Component
@ProcessingGroup("ordersProcessor")
// TODO: unit commands processor instead of Orders ?
// Alternative:
// use 1rst handler with business cmd or CRUD ingestion unit commands only (ex: ChangePersonName(newName),
// UpdatePersonInfo(newFirstName, newName...))
// use 2nd handler with order containing business commands only
public class OrderHandler {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EventBus eventBus;

    // public static void saveAndPublishOrder(final Order order) {
    // Registry.getCommandRepository()
    // .saveAll(order.getCommands().stream()
    // .map(command -> CommandAdapter.toCommandEntry(command, CommandStatus.CREATED))
    // .collect(Collectors.toList()));
    // Registry.getCommandGateway().send(order);
    // }

    @CommandHandler
    public void handle(final CommandWrapper wrapper) {
        this.logger.info("Handling command of type: " + wrapper.getCommand().getClass().getSimpleName());
        wrapper.getCommand().applyToEventStore();
    }

    @CommandHandler
    public void handle(final Order order) {
        this.logger.info("Handling Order command");
        Map<UUID, Pair<Command<?>, CommandExecutionException>> inErrorCommands = new HashMap<>();

        order.getCommands().stream().forEach(command -> {
            try {
                command.verify();
            } catch (CommandExecutionException e) {
                inErrorCommands.put(command.getCommandId(), Pair.of(command, e));
            }
        });
        if (inErrorCommands.isEmpty()) {
            order.getCommands().forEach(command -> {
                try {
                    command.applyToEventStore();
                } catch (CommandExecutionException e) {
                    inErrorCommands.put(command.getCommandId(), Pair.of(command, e));
                }
            });
            if (inErrorCommands.isEmpty()) {
                this.eventBus.publish(asEventMessage(new OrderValidated(order.getId(), order.getCommands())));
            } else {
                InvalidOrderException ex = new InvalidOrderException(order.getInfo(), inErrorCommands);
                this.eventBus.publish(asEventMessage(ex));
                this.logger.error(ex.getMessage());
                this.eventBus.publish(asEventMessage(ex));
                // TODO: in this case 2 options:
                // publish error: previous events are still applied
                // throw exception, previous events not applied but impossible to publish error (transaction
                // closed):
                // need to catch exception in service sending te command with sendAndWait and publish event
                // throw ex;
            }
        } else {
            InvalidOrderException ex = new InvalidOrderException(order.getInfo(), inErrorCommands);
            this.eventBus.publish(asEventMessage(ex));
            this.logger.error(ex.getMessage());
            // throw ex;
        }
    }
}
