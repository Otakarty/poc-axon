package poc.domain.person.events;

import poc.domain.events.DomainEvent;
import poc.domain.person.Person;
import poc.domain.person.UID;

public final class PersonCreated extends DomainEvent {
    private final UID uid;
    private Person person;

    public PersonCreated(final Person person) {
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = (prime * result) + ((this.getEventId() == null) ? 0 : this.getEventId().hashCode());
        result = (prime * result) + ((this.person == null) ? 0 : this.person.hashCode());
        result = (prime * result) + ((this.uid == null) ? 0 : this.uid.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        return true;
    }
}
