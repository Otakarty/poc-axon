package poc.application.commands;

import static org.axonframework.eventhandling.GenericEventMessage.asEventMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import poc.application.events.InvalidOrderException;
import poc.application.events.OrderValidated;

@Component
public class OrderHandler {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EventBus eventBus;

    public static void saveAndPublishOrder(final Order order) {
        Registry.getCommandRepository()
            .saveAll(order.getCommands().stream()
                .map(command -> CommandAdapter.toCommandEntry(command, CommandStatus.CREATED))
                .collect(Collectors.toList()));
        Registry.getCommandGateway().send(order);
    }

    @CommandHandler
    public void handle(final Order order) {
        this.logger.info("Handling Order command");
        Map<UUID, Pair<Command<?>, CommandExecutionException>> inErrorCommands = new HashMap<>();

        order.getCommands().stream().forEach(command -> {
            try {
                command.apply();
            } catch (CommandExecutionException e) {
                inErrorCommands.put(command.getCommandId(), Pair.of(command, e));
            }
        });

        if (inErrorCommands.isEmpty()) {
            this.eventBus.publish(asEventMessage(new OrderValidated(order.getId(), order.getCommands())));
        } else {
            InvalidOrderException ex = new InvalidOrderException(order.getInfo(), order.getId(), inErrorCommands);
            this.eventBus.publish(asEventMessage(ex));
            this.logger.error(ex.getMessage());
        }
    }
}
