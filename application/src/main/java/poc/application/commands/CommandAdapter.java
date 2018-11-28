package poc.application.commands;

import org.springframework.util.SerializationUtils;

import poc.infrastructure.CommandEntry;

public final class CommandAdapter {

    private CommandAdapter() {
    }

    public static CommandEntry toCommandEntry(final Command command, final CommandStatus status) {
        CommandEntry entry = new CommandEntry();

        entry.setCommandId(command.getCommandId().toString());
        entry.setAggregateId(command.getAggregateId().toString());
        entry.setAggregateType(command.getAggregateType().getSimpleName());
        entry.setCommandName(command.getCommandName());
        entry.setCommmand(SerializationUtils.serialize(command));
        entry.setStatus(status.toString());
        entry.setOrderId(command.getOriginOrder().getId().toString());
        // entry.setCommmandArgs(command.getC);
        return entry;
    }
}
