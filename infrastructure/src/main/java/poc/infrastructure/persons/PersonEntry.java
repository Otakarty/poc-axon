package poc.infrastructure.persons;

import javax.persistence.Entity;
import javax.persistence.Id;

import poc.domain.person.FirstName;
import poc.domain.person.Name;
import poc.domain.person.Person;
import poc.domain.person.UID;

@Entity(name = "PERSONS")
public class PersonEntry {
    @Id
    private String uid;
    private String name;
    private String firstName;

    // Mandatory default constructor
    public PersonEntry() {

    }

    public PersonEntry(final Person person) {
        this.uid = person.getUid().toString();
        this.firstName = person.getFirstName().getValue();
        this.name = person.getName().getValue();
    }

    public Person toDomainAggregate() {
        return new Person.Builder().uid(new UID(this.uid)).firstName(new FirstName(this.firstName))
            .name(new Name(this.name)).build();
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(final String myUid) {
        this.uid = myUid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String myName) {
        this.name = myName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(final String myFirstName) {
        this.firstName = myFirstName;
    }
}
