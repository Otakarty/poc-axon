package poc.domain.person.events;

import java.time.LocalDateTime;

import poc.domain.events.DomainEvent;
import poc.domain.person.Person;
import poc.domain.person.UID;

public final class PersonCreated extends DomainEvent {
    private final LocalDateTime creation;
    private final UID uid;
    private Person person;

    public PersonCreated(final Person person) {
        this.uid = person.getUid();
        this.person = person;
        this.creation = LocalDateTime.now();

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

    public LocalDateTime getCreation() {
        return this.creation;
    }

    @Override
    public String getEventName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String toString() {
        return "PersonCreated [creation=" + this.creation + ", uid=" + this.uid + ", person=" + this.person + "]";
    }
}
