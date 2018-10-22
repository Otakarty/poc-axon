package poc.domain.person.events;

import java.time.LocalDateTime;

import poc.domain.person.Name;
import poc.domain.person.UID;

public final class PersonNameChanged {
    public final String eventName = this.getClass().getSimpleName();
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
}
