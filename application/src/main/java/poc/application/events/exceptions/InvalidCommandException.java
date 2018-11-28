package poc.application.events.exceptions;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

import org.axonframework.commandhandling.CommandExecutionException;

import poc.application.commands.Command;

public class InvalidCommandException extends CommandExecutionException {

    private static final long serialVersionUID = 1212145618029004357L;

    private final Command command;

    private final List<CommandExecutionException> exceptions;

    public InvalidCommandException(final Command command, final List<CommandExecutionException> exceptions) {
        super(MessageFormat.format("Invalid command {0}, causes: {1}", command.getCommandId(),
            exceptions.parallelStream().map(CommandExecutionException::getMessage).collect(Collectors.toList())),
            null);
        this.command = command;
        this.exceptions = exceptions;

    }

    public final Command getCommand() {
        return this.command;
    }

    public final List<CommandExecutionException> getExceptions() {
        return this.exceptions;
    }

}
