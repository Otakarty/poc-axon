package poc.domain.person;

import java.io.Serializable;

import poc.domain.NullableObject;

public class IngestionPersonDpo implements Serializable {
    private static final long serialVersionUID = 5490118119545116840L;
    private final NullableObject<FirstName> firstName;
    private final NullableObject<Name> name;

    private IngestionPersonDpo(final Builder builder) {
        this.firstName = builder.firstName;
        this.name = builder.name;
    }

    public final NullableObject<FirstName> getFirstName() {
        return this.firstName;
    }

    public final NullableObject<Name> getName() {
        return this.name;
    }

    public static class Builder {
        private NullableObject<FirstName> firstName;
        private NullableObject<Name> name;

        public final NullableObject<FirstName> getFirstName() {
            return this.firstName;
        }

        public final Builder firstName(final NullableObject<FirstName> firstName) {
            this.firstName = firstName;
            return this;
        }

        public final NullableObject<Name> getName() {
            return this.name;
        }

        public final Builder name(final NullableObject<Name> name) {
            this.name = name;
            return this;
        }

        public IngestionPersonDpo build() {
            return new IngestionPersonDpo(this);
        }
    }

    @Override
    public String toString() {
        return "IngestionPersonDpo [firstName=" + this.firstName + ", name=" + this.name + "]";
    }
}
