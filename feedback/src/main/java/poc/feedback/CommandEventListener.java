package poc.feedback;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import poc.application.events.OrderValidated;
import poc.application.events.exceptions.InvalidCommandException;
import poc.infrastructure.CommandJpaRepository;

@Component
@ProcessingGroup("feedbacksProcessor")
public class CommandEventListener {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CommandJpaRepository commandJpaRepository;

    @EventHandler
    protected void on(final OrderValidated event) {
        this.logger.info("Feedback : Handling OrderValidated event");

        // TODO: save Order status FINISHED
    }

    @EventHandler
    protected void on(final InvalidCommandException exception) {
        this.logger.info("Handling InvalidCommandException, KO feedback to submit");
        this.logger.info("Exceptions: " + exception.getExceptions());

        // set commands status to FAILED
        // List<CommandEntry> invalidCommands =
        // this.commandJpaRepository.findAllByOrderId(exception.getOrigin().getId().toString());
        // invalidCommands.stream().forEach(command -> {
        // command.setStatus(CommandStatus.FAILED.toString());
        // Pair<Command, CommandExecutionException> commandInError =
        // exception.getInErrorCommands().get(UUID.fromString(command.getCommandId()));
        // if (commandInError != null) {
        // command.setDetail(commandInError.getSecond().getMessage());
        // }
        // });

        // TODO: save order status to FAILED instead of commands
        // this.commandJpaRepository.saveAll(invalidCommands);
    }
}
