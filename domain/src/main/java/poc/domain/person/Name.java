package poc.domain.person;

import java.io.Serializable;

import org.springframework.util.Assert;

public final class Name implements Serializable {
	private static final long serialVersionUID = 417021896757251173L;

	private final String value;

	public Name(String name) {
		Assert.hasLength(name, "Missing name");
		this.value = name;
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
		Name other = (Name) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
