package poc.application.commands;

import java.time.LocalDateTime;
import java.util.UUID;

public class OrderInfo {
    private final UUID id;
    private final LocalDateTime dtCreation;
    private final ServiceEnum source;

    public OrderInfo(final UUID id, final ServiceEnum source) {
        this.id = id;
        this.dtCreation = LocalDateTime.now();
        this.source = source;
    }

    public final UUID getId() {
        return this.id;
    }

    public final LocalDateTime getDtCreation() {
        return this.dtCreation;
    }

    public final ServiceEnum getSource() {
        return this.source;
    }
}
