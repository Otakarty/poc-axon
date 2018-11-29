package poc.domain.person;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import poc.domain.exceptions.InvalidIngestionCommandException;
import poc.domain.exceptions.WhiteEventException;
import poc.domain.person.events.PersonCreated;
import poc.domain.person.events.PersonNameChanged;
import poc.domain.person.events.PersonUpdatedFromIngestion;

@Aggregate
public class Person extends poc.domain.Aggregate<UID> implements Serializable {
    private static final long serialVersionUID = -3175513689962156083L;

    @AggregateIdentifier
    private UID uid;
    private Name name;
    private FirstName firstName;

    // Mandatory for fixture test
    public Person() {

    }

    private Person copy(final Person other) {
        this.uid = other.getId();
        this.firstName = other.getFirstName();
        this.name = other.getName();
        return this;
    }

    public Person(final Person.Builder builder) {
        this.uid = builder.getUid();
        this.name = builder.getName();
        this.firstName = builder.getFirstName();
    }

    public void changeName(final Name newName) throws WhiteEventException {
        Assert.isTrue(newName != null, "New name should not be null");
        if (this.getName().equals(newName)) {
            throw new WhiteEventException("new name is no different");
        }
        this.name = newName;
    }

    public void changeFirstName(final FirstName newFirstName) throws WhiteEventException {
        Assert.isTrue(newFirstName != null, "New firstName should not be null");
        if (this.getFirstName().equals(newFirstName)) {
            throw new WhiteEventException("new firstName is no different");
        }
        this.firstName = newFirstName;
    }

    public void updateFromIngestion(final IngestionPersonDpo person) throws InvalidIngestionCommandException {
        Person copy = new Person().copy(this);
        List<Consumer<Person>> toApply = Arrays.asList(p -> p.changeFirstName(person.getFirstName().orElse(null)),
            p -> this.changeName(person.getName().orElse(null)));
        List<IllegalArgumentException> exceptions = new ArrayList<>();
        AtomicInteger whiteEventsCount = new AtomicInteger();

        toApply.forEach(m -> {
            try {
                m.accept(this);
            } catch (IllegalArgumentException e) {
                exceptions.add(e);
            } catch (WhiteEventException e) {
                whiteEventsCount.incrementAndGet();
            }
        });
        if (!exceptions.isEmpty()) {
            // Rollback
            this.copy(copy);
            throw new InvalidIngestionCommandException(exceptions);
        }
        if (whiteEventsCount.intValue() == toApply.size()) {
            throw new WhiteEventException("Ingestion does not modify person");
        }
    }

    @Override
    public UID getId() {
        return this.uid;
    }

    public final Name getName() {
        return this.name;
    }

    public final FirstName getFirstName() {
        return this.firstName;
    }

    /******** Event Sourcing Handlers. ********/
    transient Logger logger = LoggerFactory.getLogger(this.getClass());

    @EventSourcingHandler
    protected void on(final PersonCreated event) {
        this.logger.info("Event source handler on PersonCreated event");
        this.uid = event.getUid();
        this.name = event.getPerson().getName();
        this.firstName = event.getPerson().getFirstName();
    }

    @EventSourcingHandler
    protected void on(final PersonNameChanged event) throws WhiteEventException {
        this.logger.info("Event source handler on PersonNameChanged event");
        this.changeName(event.getName());
    }

    @EventSourcingHandler
    protected void on(final PersonUpdatedFromIngestion event) throws InvalidIngestionCommandException {
        this.logger.info("Event source handler on PersonUpdatedFromIngestion event");
        this.updateFromIngestion(event.getNewPersonPayload());
    }

    /****** End Event Sourcing Handlers. ******/

    @Override
    public int hashCode() {
        return Objects.hash(this.uid, this.firstName, this.name);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Person)) {
            return false;
        }

        Person other = (Person) obj;
        return Objects.equals(this.uid, other.uid) && Objects.equals(this.name, other.name)
            && Objects.equals(this.firstName, other.firstName);
    }

    public static class Builder {
        private UID uid;
        private Name name;
        private FirstName firstName;

        public Builder uid(final UID uid) {
            this.uid = uid;
            return this;
        }

        public Builder name(final Name name) {
            this.name = name;
            return this;
        }

        public Builder firstName(final FirstName firstName) {
            this.firstName = firstName;
            return this;
        }

        public Person build() {
            return new Person(this);
        }

        public UID getUid() {
            return this.uid;
        }

        public Name getName() {
            return this.name;
        }

        public FirstName getFirstName() {
            return this.firstName;
        }
    }

}
