package poc.domain.person;

import java.util.List;

public interface Persons {
    Person findById(UID id);

    Person add(Person person);

    // TODO: list all methods (changeName, changeFirstName...) or unique save(Person p) to handle modifications ?
    Person changeName(UID id, Name newName);

    Person updateInfo(Person p);
    // end TODO

    List<Person> findAll();

    Long totalCount();
}
