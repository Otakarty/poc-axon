package poc.domain.person.events;

import java.time.LocalDateTime;
import java.util.UUID;

import poc.domain.events.DomainEvent;
import poc.domain.person.Name;
import poc.domain.person.UID;

public final class PersonNameChanged extends DomainEvent {
    // TODO: duplicate field to remove
    // Warning: if removed, could not deserialize anymore
    // Solution: snapshot or upcast
    private final LocalDateTime creation;
    private final UID uid;
    private final Name name;

    public PersonNameChanged(final UUID commandId, final UID uid, final Name name) {
        super(commandId);
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

    @Override
    public String toString() {
        return "PersonNameChanged [creation=" + this.creation + ", uid=" + this.uid + ", name=" + this.name + "]";
    }
}
