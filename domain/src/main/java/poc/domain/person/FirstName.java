package poc.domain.person;

import java.io.Serializable;

import org.springframework.util.Assert;

public final class FirstName implements Serializable {
    private static final long serialVersionUID = 3840840767512690912L;

    private final String value;

    public FirstName(final String firstName) {
        Assert.hasLength(firstName, "Missing firstName");
        this.value = firstName;
    }

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
    public String toString() {
        return this.value;
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
        FirstName other = (FirstName) obj;
        if (this.value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!this.value.equals(other.value)) {
            return false;
        }
        return true;
    }

}
