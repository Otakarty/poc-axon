package poc.domain.person.commands;

import org.axonframework.commandhandling.TargetAggregateIdentifier;
import org.springframework.util.Assert;

import poc.domain.person.Name;
import poc.domain.person.UID;

public final class ChangePersonName {
	@TargetAggregateIdentifier
	private final UID uid;
	private final Name name;

	public ChangePersonName(UID uid, Name name) {
		Assert.notNull(uid, "UID is mandatory");
		Assert.notNull(name, "Name is mandatory");
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
