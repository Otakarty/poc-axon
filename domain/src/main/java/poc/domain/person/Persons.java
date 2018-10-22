package poc.domain.person;

public interface Persons {
    Person findById(UID id);

    Person create(Person person);

    Person save(Person person);
}
