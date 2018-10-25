package poc.domain.person;

import java.io.Serializable;

import org.springframework.util.Assert;

public final class Name implements Serializable {
    private static final long serialVersionUID = 417021896757251173L;

    private final String value;

    public Name(final String name) {
        Assert.hasLength(name, "Missing name");
        this.value = name;
    }

    public final String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
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
        Name other = (Name) obj;
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
