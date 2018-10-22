package poc.application.person;

import poc.domain.person.Person;

public final class PersonDTO {

    private String uid;
    private String name;
    private String firstName;

    public PersonDTO() {
    }

    public PersonDTO(final String uid, final String name, final String firstName) {
        this.uid = uid;
        this.name = name;
        this.firstName = firstName;
    }

    public PersonDTO(final Person person) {
        this.uid = person.getUid().getValue();
        this.name = person.getName().getValue();
        this.firstName = person.getFirstName().getValue();
    }

    public final String getUid() {
        return this.uid;
    }

    public final void setUid(final String uid) {
        this.uid = uid;
    }

    public final String getName() {
        return this.name;
    }

    public final void setName(final String name) {
        this.name = name;
    }

    public final String getFirstName() {
        return this.firstName;
    }

    public final void setFirstName(final String firstName) {
        this.firstName = firstName;
    }
}
