package poc.application.commands;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class OrderInfo implements Serializable {
    private static final long serialVersionUID = -6567393086766682750L;
    private final UUID id;
    private final LocalDateTime dtCreation;
    private final ServiceEnum source;

    public OrderInfo(final ServiceEnum source) {
        this.id = UUID.randomUUID();
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

    @Override
    public String toString() {
        return "OrderInfo [id=" + this.id + ", dtCreation=" + this.dtCreation + ", source=" + this.source + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((this.dtCreation == null) ? 0 : this.dtCreation.hashCode());
        result = (prime * result) + ((this.id == null) ? 0 : this.id.hashCode());
        result = (prime * result) + ((this.source == null) ? 0 : this.source.hashCode());
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
        OrderInfo other = (OrderInfo) obj;
        if (this.dtCreation == null) {
            if (other.dtCreation != null) {
                return false;
            }
        } else if (!this.dtCreation.equals(other.dtCreation)) {
            return false;
        }
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!this.id.equals(other.id)) {
            return false;
        }
        if (this.source != other.source) {
            return false;
        }
        return true;
    }
}
