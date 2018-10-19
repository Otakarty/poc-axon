package poc.domain.person.commands;

import org.axonframework.commandhandling.TargetAggregateIdentifier;
import org.springframework.util.Assert;

import poc.domain.person.FirstName;
import poc.domain.person.Name;
import poc.domain.person.UID;

public final class CreatePerson {
	@TargetAggregateIdentifier
	private final UID uid;
	private final Name name;
	private final FirstName firstName;

	public CreatePerson(UID uid, Name name, FirstName firstName) {
		Assert.notNull(uid, "UID is mandatory");
		Assert.notNull(name, "Name is mandatory");
		Assert.notNull(firstName, "First name is mandatory");
		this.uid = uid;
		this.name = name;
		this.firstName = firstName;
	}

	public final UID getUid() {
		return uid;
	}

	public final Name getName() {
		return name;
	}

	public final FirstName getFirstName() {
		return firstName;
	}
}
