package poc.domain.person;

import java.io.Serializable;

import org.springframework.util.Assert;

public final class UID implements Serializable {
	private static final long serialVersionUID = -2168741636320165596L;

	private final String value;

	public UID(String uid) {
		Assert.hasLength(uid, "Missing uid");
		this.value = uid;
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
		UID other = (UID) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return getValue();
	}
}
