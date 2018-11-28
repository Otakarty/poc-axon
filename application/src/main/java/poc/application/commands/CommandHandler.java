package poc.application.commands;

import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.config.ProcessingGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("commandsProcessor")
public class CommandHandler {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @org.axonframework.commandhandling.CommandHandler
    public void handle(final CommandWrapper wrapper) {
        this.logger.info("Handling command of type: " + wrapper.getCommand().getClass().getSimpleName());
        // TODO: try catch to send exception in bus or throw InvalidCommand for all commands when error
        try {
            wrapper.getCommand().apply();
        } catch (CommandExecutionException e) {
            this.logger.error(e.getMessage());
            // TODO: send InvalidCommand in bus
        }
    }

}
