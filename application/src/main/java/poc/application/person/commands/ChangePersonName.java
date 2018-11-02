package poc.application.person.commands;

import org.axonframework.commandhandling.CommandExecutionException;
import org.springframework.util.Assert;

import poc.application.commands.OrderInfo;
import poc.application.commands.UpdateCommand;
import poc.application.person.commands.exceptions.CannotChangeNameException;
import poc.domain.events.DomainEvent;
import poc.domain.person.Name;
import poc.domain.person.Person;
import poc.domain.person.UID;
import poc.domain.person.events.PersonNameChanged;

public final class ChangePersonName extends UpdateCommand<Person> {
    private final Name name;

    public ChangePersonName(final OrderInfo originOrder, final UID uid, final Name name) {
        super(originOrder, uid);
        Assert.notNull(uid, "UID is mandatory");
        Assert.notNull(name, "Name is mandatory");
        this.name = name;
    }

    // public ChangePersonName(final UID uid, final Name name) {
    // super(new OrderInfo(ServiceEnum.IHM), uid);
    // Assert.notNull(uid, "UID is mandatory");
    // Assert.notNull(name, "Name is mandatory");
    // this.name = name;
    // }

    public final Name getName() {
        return this.name;
    }

    @Override
    public String getCommandName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public CommandExecutionException exceptionToThrow(final String message) {
        return new CannotChangeNameException(this.aggregateId, this.name, message);
    }

    @Override
    public DomainEvent getDomainEvent() {
        return new PersonNameChanged(this.commandId, this.aggregateId, this.name);
    }

    @Override
    public void apply(final Person p) throws RuntimeException {
        p.changeName(this.name);
    }

    @Override
    public String toString() {
        return "ChangePersonName [name=" + this.name + ", getDomainEvent()=" + this.getDomainEvent()
            + ", getOriginOrder()=" + this.getOriginOrder() + ", getId()=" + this.getAggregateId() + "]";
    }

}
