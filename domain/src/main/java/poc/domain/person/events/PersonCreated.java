package poc.domain.person.events;

import poc.domain.person.FirstName;
import poc.domain.person.Name;
import poc.domain.person.UID;

public final class PersonCreated {
	private final UID uid;
	private final Name name;
	private final FirstName firstName;

	public PersonCreated(UID uid, Name name, FirstName firstName) {
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
