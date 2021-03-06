package poc.domain.person;

import java.io.Serializable;

import org.springframework.util.Assert;

import poc.domain.AggregateId;

public final class UID extends AggregateId<String, UID> implements Serializable {
    private static final long serialVersionUID = -2168741636320165596L;

    private final String value;

    public UID(final String uid) {
        Assert.hasLength(uid, "Missing uid");
        this.value = uid.toLowerCase();
    }

    @Override
    public final String getValue() {
        return this.value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((this.value == null) ? 0 : this.value.hashCode());
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
        UID other = (UID) obj;
        if (this.value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!this.value.equals(other.value)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.value;
    }

}
