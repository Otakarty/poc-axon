package poc.domain.events;

public abstract class DomainEvent {
    public abstract String getEventName();

    @Override
    public String toString() {
        return "DomainEvent [getEventName()=" + this.getEventName() + "]";
    }

}
