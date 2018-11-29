package poc.application.person.commands;

import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.TargetAggregateIdentifier;
import org.springframework.util.Assert;

import poc.application.commands.CreateCommand;
import poc.application.commands.CommandInfo;
import poc.application.person.commands.exceptions.CannotCreatePersonException;
import poc.domain.events.DomainEvent;
import poc.domain.person.Person;
import poc.domain.person.UID;
import poc.domain.person.events.PersonCreated;

public final class CreatePerson extends CreateCommand<UID, Person> {

    private static final long serialVersionUID = -607036092546578183L;

    public final String commandName = this.getClass().getSimpleName();
    @TargetAggregateIdentifier
    private final UID uid;

    private final Person person;

    public CreatePerson(final CommandInfo originOrder, final Person person) {
        super(originOrder, person);
        Assert.notNull(person.getId(), "UID is mandatory");
        Assert.notNull(person.getName(), "Name is mandatory");
        Assert.notNull(person.getFirstName(), "First name is mandatory");
        this.uid = person.getId();
        this.person = person;
    }

    public final UID getUid() {
        return this.uid;
    }

    public Person getPerson() {
        return this.person;
    }

    @Override
    protected CommandExecutionException exceptionToThrow(final String causeMessage, final Throwable cause) {
        return new CannotCreatePersonException(this.uid, causeMessage, cause);
    }

    @Override
    protected DomainEvent getDomainEvent() {
        return new PersonCreated(this.getCommandId(), this.person);
    }

    @Override
    public String toString() {
        return "CreatePerson [uid=" + this.uid + ", person=" + this.person + "]";
    }
}
