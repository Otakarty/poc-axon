package poc.domain.person;

import java.io.Serializable;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.util.Assert;

import poc.domain.person.commands.ChangePersonName;
import poc.domain.person.commands.CreatePerson;
import poc.domain.person.events.PersonCreated;
import poc.domain.person.events.PersonNameChanged;

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

	public Person(Person.Builder builder) {
		this.uid = builder.getUid();
		this.name = builder.getName();
		this.firstName = builder.getFirstName();
	}

	public final Name getName() {
		return name;
	}

	public final FirstName getFirstName() {
		return firstName;
	}

	/******** Commands Handlers. ********/
	@CommandHandler
	public Person(CreatePerson command) {
		AggregateLifecycle.apply(new PersonCreated(command.getUid(), command.getName(), command.getFirstName()));
	}

	@CommandHandler
	protected void on(ChangePersonName command) {
		Assert.isTrue(this.getName().equals(command.getName()), "New name should be different");
		this.name = command.getName();

		AggregateLifecycle.apply(new PersonNameChanged(this.uid, this.name));
	}

	/******** Event Sourcing Handlers. ********/
	@EventSourcingHandler
	protected void on(PersonCreated event) {
		this.uid = event.getUid();
		this.name = event.getName();
		this.firstName = event.getFirstName();
	}

	@EventSourcingHandler
	protected void on(PersonNameChanged event) {
		this.uid = event.getUid();
		this.name = event.getName();
	}

	public class Builder {
		private UID uid;
		private Name name;
		private FirstName firstName;

		public Builder uid(UID uid) {
			this.uid = uid;
			return this;
		}

		public Builder name(Name name) {
			this.name = name;
			return this;
		}

		public Builder firstName(FirstName firstName) {
			this.firstName = firstName;
			return this;
		}

		public Person build() {
			return new Person(this);
		}

		public UID getUid() {
			return uid;
		}

		public Name getName() {
			return name;
		}

		public FirstName getFirstName() {
			return firstName;
		}
	}

}
