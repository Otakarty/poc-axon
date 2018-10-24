package poc.application.events;

import java.util.List;

import poc.domain.events.DomainEvent;
import poc.domain.person.UID;

public final class OrderValidated {
    private final UID id;
    private final List<DomainEvent> eventsToApply;

    public OrderValidated(final UID id, final List<DomainEvent> eventsToApply) {
        this.id = id;
        this.eventsToApply = eventsToApply;
    }

    public UID getId() {
        return this.id;
    }

    public List<DomainEvent> getEventsToApply() {
        return this.eventsToApply;
    }
}
