package poc.domain.person.events;

import java.time.LocalDateTime;

import poc.domain.person.Person;
import poc.domain.person.UID;

public final class PersonCreated {
    public final String eventName = this.getClass().getSimpleName();
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
}
