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
import poc.domain.person.events.WhiteEventException;

public final class ChangePersonName extends UpdateCommand<Person> {
    private static final long serialVersionUID = 8622131695860114204L;

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
    public CommandExecutionException exceptionToThrow(final String message, final Throwable cause) {
        return new CannotChangeNameException(this.aggregateId, this.name, message, cause);
    }

    @Override
    public DomainEvent getDomainEvent() {
        return new PersonNameChanged(this.commandId, this.aggregateId, this.name);
    }

    @Override
    public void verify(final Person p) throws WhiteEventException {
        p.nameCanBeChangedWith(this.name);
    }

    @Override
    public String toString() {
        return "ChangePersonName [name=" + this.name + ", getId()=" + this.getAggregateId() + "]";
    }

}
