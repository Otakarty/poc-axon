package application.person;

public final class PersonDTO {

	private String uid;
	private String name;
	private String firstName;

	public PersonDTO() {
	}

	public PersonDTO(String uid, String name, String firstName) {
		this.uid = uid;
		this.name = name;
		this.firstName = firstName;
	}

	public final String getUid() {
		return uid;
	}

	public final void setUid(String uid) {
		this.uid = uid;
	}

	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	public final String getFirstName() {
		return firstName;
	}

	public final void setFirstName(String firstName) {
		this.firstName = firstName;
	}
}
