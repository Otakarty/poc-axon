package poc.feedback;

import java.util.List;
import java.util.UUID;

import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import poc.application.commands.Command;
import poc.application.commands.CommandStatus;
import poc.application.events.OrderValidated;
import poc.application.events.exceptions.InvalidOrderException;
import poc.infrastructure.CommandEntry;
import poc.infrastructure.CommandJpaRepository;

@Component
@ProcessingGroup("feedbacksProcessor")
public class OrderEventListener {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CommandJpaRepository commandJpaRepository;

    @EventHandler
    protected void on(final OrderValidated event) {
        this.logger.info("Feedback : Handling OrderValidated event");

        // TODO: save Order status FINISHED
    }

    @EventHandler
    protected void on(final InvalidOrderException exception) {
        this.logger.info("Handling InvalidOrderException for new refog, KO feedback to submit");
        this.logger.info("Commands in error: " + exception.getInErrorCommands());

        // set commands status to FAILED
        List<CommandEntry> invalidCommands =
            this.commandJpaRepository.findAllByOrderId(exception.getOrigin().getId().toString());
        invalidCommands.stream().forEach(command -> {
            command.setStatus(CommandStatus.FAILED.toString());
            Pair<Command, CommandExecutionException> commandInError =
                exception.getInErrorCommands().get(UUID.fromString(command.getCommandId()));
            if (commandInError != null) {
                command.setDetail(commandInError.getSecond().getMessage());
            }
        });
        // TODO: save order status to FAILED instead of commands
        // this.commandJpaRepository.saveAll(invalidCommands);
    }
}
