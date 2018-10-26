package poc.domain.events;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class DomainEvent {
    public final UUID eventId;
    private final LocalDateTime creation;

    public DomainEvent() {
        this.eventId = UUID.randomUUID();
        this.creation = LocalDateTime.now();
    }

    public final UUID getEventId() {
        return this.eventId;
    }

    public final LocalDateTime getCreation() {
        return this.creation;
    }

    public abstract String getEventName();

    @Override
    public String toString() {
        return "DomainEvent [getEventName()=" + this.getEventName() + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((this.eventId == null) ? 0 : this.eventId.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        DomainEvent other = (DomainEvent) obj;
        if (this.eventId == null) {
            if (other.eventId != null) {
                return false;
            }
        } else if (!this.eventId.equals(other.eventId)) {
            return false;
        }
        return true;
    }
}
