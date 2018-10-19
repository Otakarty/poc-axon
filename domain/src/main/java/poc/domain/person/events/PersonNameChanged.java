package poc.domain.person.events;

import poc.domain.person.Name;
import poc.domain.person.UID;

public final class PersonNameChanged {
	private final UID uid;
	private final Name name;

	public PersonNameChanged(UID uid, Name name) {
		this.uid = uid;
		this.name = name;
	}

	public final UID getUid() {
		return uid;
	}

	public final Name getName() {
		return name;
	}
}
