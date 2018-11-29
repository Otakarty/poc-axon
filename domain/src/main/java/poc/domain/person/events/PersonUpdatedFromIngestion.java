package poc.domain.person.events;

import java.util.UUID;

import poc.domain.events.DomainEvent;
import poc.domain.person.IngestionPersonDpo;
import poc.domain.person.UID;

public class PersonUpdatedFromIngestion extends DomainEvent {
    private final UID uid;
    private final IngestionPersonDpo newPersonPayload;

    public PersonUpdatedFromIngestion(final UUID commandId, final UID uid,
        final IngestionPersonDpo newPersonPayload) {
        super(commandId);
        this.uid = uid;
        this.newPersonPayload = newPersonPayload;
    }

    public final UID getUid() {
        return this.uid;
    }

    public final IngestionPersonDpo getNewPersonPayload() {
        return this.newPersonPayload;
    }
}
