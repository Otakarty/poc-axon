package poc.application.person;

import poc.domain.person.FirstName;
import poc.domain.person.Name;
import poc.domain.person.Person;
import poc.domain.person.UID;

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
        this.uid = person.getId().getValue();
        this.name = person.getName().getValue();
        this.firstName = person.getFirstName().getValue();
    }

    public Person toDomainEntity() {
        return new Person.Builder().uid(new UID(this.getUid())).firstName(new FirstName(this.getFirstName()))
            .name(new Name(this.getName())).build();
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
