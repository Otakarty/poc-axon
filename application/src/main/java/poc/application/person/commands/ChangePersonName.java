package poc.application.person.commands;

import org.axonframework.commandhandling.TargetAggregateIdentifier;
import org.springframework.util.Assert;

import poc.domain.person.Name;
import poc.domain.person.UID;

public final class ChangePersonName {
    public final String commandName = this.getClass().getSimpleName();
    @TargetAggregateIdentifier
    private final UID uid;
    private final Name name;

    public ChangePersonName(final UID uid, final Name name) {
        Assert.notNull(uid, "UID is mandatory");
        Assert.notNull(name, "Name is mandatory");
        this.uid = uid;
        this.name = name;
    }

    public final UID getUid() {
        return this.uid;
    }

    public final Name getName() {
        return this.name;
    }

}
