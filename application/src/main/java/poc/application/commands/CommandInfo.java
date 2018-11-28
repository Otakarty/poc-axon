package poc.application.commands;

import java.io.Serializable;
import java.time.LocalDateTime;

public class CommandInfo implements Serializable {
    private static final long serialVersionUID = -6567393086766682750L;
    private final LocalDateTime dtCreation;
    private final ServiceEnum source;

    public CommandInfo(final ServiceEnum source) {
        this.dtCreation = LocalDateTime.now();
        this.source = source;
    }

    public final LocalDateTime getDtCreation() {
        return this.dtCreation;
    }

    public final ServiceEnum getSource() {
        return this.source;
    }

    @Override
    public String toString() {
        return "OrderInfo [dtCreation=" + this.dtCreation + ", source=" + this.source + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((this.dtCreation == null) ? 0 : this.dtCreation.hashCode());
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
        CommandInfo other = (CommandInfo) obj;
        if (this.dtCreation == null) {
            if (other.dtCreation != null) {
                return false;
            }
        } else if (!this.dtCreation.equals(other.dtCreation)) {
            return false;
        }
        if (this.source != other.source) {
            return false;
        }
        return true;
    }
}
