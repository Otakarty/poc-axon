package poc.domain.person.events;

import java.util.UUID;

import poc.domain.events.DomainEvent;
import poc.domain.person.Person;
import poc.domain.person.UID;

public final class PersonCreated extends DomainEvent {
    private final UID uid;
    private Person person;

    public PersonCreated(final UUID commandId, final Person person) {
        super(commandId);
        this.uid = person.getUid();
        this.person = person;
    }

    public final UID getUid() {
        return this.uid;
    }

    public Person getPerson() {
        return this.person;
    }

    public void setPerson(final Person myPerson) {
        this.person = myPerson;
    }

    @Override
    public String getEventName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String toString() {
        return "PersonCreated [creation=" + this.getCreation() + ", uid=" + this.uid + ", person=" + this.person
            + "]";
    }
}
