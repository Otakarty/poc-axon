package poc.application.commands;

public abstract class Command {
    private final OrderInfo originOrder;

    public Command(final OrderInfo originOrder) {
        this.originOrder = originOrder;
    }

    public OrderInfo getOriginOrder() {
        return this.originOrder;
    }

    public abstract String getCommandName();
}
