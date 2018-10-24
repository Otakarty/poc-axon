package poc.application.person.commands;

import java.util.UUID;

import org.axonframework.commandhandling.TargetAggregateIdentifier;
import org.springframework.util.Assert;

import poc.application.commands.Command;
import poc.application.commands.OrderInfo;
import poc.application.commands.ServiceEnum;
import poc.domain.person.Person;
import poc.domain.person.UID;

public final class CreatePerson extends Command {
    public final String commandName = this.getClass().getSimpleName();
    @TargetAggregateIdentifier
    private final UID uid;

    private final Person person;

    public CreatePerson(final OrderInfo originOrder, final Person person) {
        super(originOrder);
        Assert.notNull(person.getUid(), "UID is mandatory");
        Assert.notNull(person.getName(), "Name is mandatory");
        Assert.notNull(person.getFirstName(), "First name is mandatory");
        this.uid = person.getUid();
        this.person = person;
    }

    public CreatePerson(final Person person) {
        super(new OrderInfo(UUID.randomUUID(), ServiceEnum.IHM));
        Assert.notNull(person.getUid(), "UID is mandatory");
        Assert.notNull(person.getName(), "Name is mandatory");
        Assert.notNull(person.getFirstName(), "First name is mandatory");
        this.uid = person.getUid();
        this.person = person;
    }

    public final UID getUid() {
        return this.uid;
    }

    public Person getPerson() {
        return this.person;
    }

    @Override
    public String getCommandName() {
        return this.getClass().getSimpleName();
    }
}
