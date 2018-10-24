package poc.application.person.commands;

import java.util.UUID;

import org.axonframework.commandhandling.TargetAggregateIdentifier;
import org.springframework.util.Assert;

import poc.application.commands.Command;
import poc.application.commands.OrderInfo;
import poc.application.commands.ServiceEnum;
import poc.domain.person.Name;
import poc.domain.person.UID;

public final class ChangePersonName extends Command {
    @TargetAggregateIdentifier
    private final UID uid;
    private final Name name;

    public ChangePersonName(final OrderInfo originOrder, final UID uid, final Name name) {
        super(originOrder);
        Assert.notNull(uid, "UID is mandatory");
        Assert.notNull(name, "Name is mandatory");
        this.uid = uid;
        this.name = name;
    }

    public ChangePersonName(final UID uid, final Name name) {
        super(new OrderInfo(UUID.randomUUID(), ServiceEnum.IHM));
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

    @Override
    public String getCommandName() {
        return this.getClass().getSimpleName();
    }
}
