package poc.domain.person;

import java.io.Serializable;

import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import poc.domain.person.events.PersonCreated;
import poc.domain.person.events.PersonNameChanged;
import poc.domain.person.events.WhiteEventException;

@Aggregate
public class Person implements Serializable {
    private static final long serialVersionUID = -3175513689962156083L;

    @AggregateIdentifier
    private UID uid;
    private Name name;
    private FirstName firstName;

    // Mandatory
    public Person() {

    }

    public Person(final Person.Builder builder) {
        this.uid = builder.getUid();
        this.name = builder.getName();
        this.firstName = builder.getFirstName();
    }

    public void nameCanBeChangedWith(final Name newName) throws WhiteEventException {
        Assert.isTrue(newName != null, "New name should not be null");
        if (this.getName().equals(newName)) {
            throw new WhiteEventException("new name is no different");
        }
    }

    public void changeName(final Name newName) throws WhiteEventException {
        this.nameCanBeChangedWith(newName);
        this.name = newName;
    }

    public UID getUid() {
        return this.uid;
    }

    public final Name getName() {
        return this.name;
    }

    public final FirstName getFirstName() {
        return this.firstName;
    }

    /******** Event Sourcing Handlers. ********/
    Logger logger = LoggerFactory.getLogger(this.getClass());

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

    /****** End Event Sourcing Handlers. ******/

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((this.firstName == null) ? 0 : this.firstName.hashCode());
        result = (prime * result) + ((this.name == null) ? 0 : this.name.hashCode());
        result = (prime * result) + ((this.uid == null) ? 0 : this.uid.hashCode());
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
        Person other = (Person) obj;
        if (this.firstName == null) {
            if (other.firstName != null) {
                return false;
            }
        } else if (!this.firstName.equals(other.firstName)) {
            return false;
        }
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        if (this.uid == null) {
            if (other.uid != null) {
                return false;
            }
        } else if (!this.uid.equals(other.uid)) {
            return false;
        }
        return true;
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
