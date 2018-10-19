package poc.domain.person;

import java.io.Serializable;

import org.springframework.util.Assert;

public final class FirstName implements Serializable {
	private static final long serialVersionUID = 3840840767512690912L;

	private final String value;

	public FirstName(String firstName) {
		Assert.hasLength(firstName, "Missing firstName");
		this.value = firstName;
	}

	public final String getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FirstName other = (FirstName) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
