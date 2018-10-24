package poc.domain.person;

import java.util.List;

public interface Persons {
    Person findById(UID id);

    Person create(Person person);

    Person save(Person person);

    List<Person> findAll();
}
