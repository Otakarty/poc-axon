package poc.application.person.commands;

import org.axonframework.commandhandling.TargetAggregateIdentifier;
import org.springframework.util.Assert;

import poc.domain.person.Person;
import poc.domain.person.UID;

public final class CreatePerson {
    public final String commandName = this.getClass().getSimpleName();
    @TargetAggregateIdentifier
    private final UID uid;

    private final Person person;

    public CreatePerson(final Person person) {
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
}
