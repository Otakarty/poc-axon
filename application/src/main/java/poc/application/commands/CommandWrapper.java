package poc.application.commands;

public class CommandWrapper {
    private final Command command;

    public CommandWrapper(final Command command) {
        this.command = command;
    }

    public final Command getCommand() {
        return this.command;
    }

}
