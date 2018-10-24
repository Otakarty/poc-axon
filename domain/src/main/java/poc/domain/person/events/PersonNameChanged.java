package poc.domain.person.events;

import java.time.LocalDateTime;

import poc.domain.events.DomainEvent;
import poc.domain.person.Name;
import poc.domain.person.UID;

public final class PersonNameChanged extends DomainEvent {
    private final LocalDateTime creation;
    private final UID uid;
    private final Name name;

    public PersonNameChanged(final UID uid, final Name name) {
        this.uid = uid;
        this.name = name;
        this.creation = LocalDateTime.now();
    }

    public final UID getUid() {
        return this.uid;
    }

    public final Name getName() {
        return this.name;
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
        return "PersonNameChanged [creation=" + this.creation + ", uid=" + this.uid + ", name=" + this.name + "]";
    }
}
