package poc.infrastructure;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import poc.domain.person.Person;
import poc.domain.person.Persons;
import poc.domain.person.UID;

@Repository
public class PersonsRepository implements Persons {

    private static Map<UID, Person> inMemoryRepository = new HashMap<>();

    @Override
    public Person findById(final UID id) {
        return Optional.ofNullable(inMemoryRepository.get(id)).orElseThrow(
            () -> new IllegalArgumentException(MessageFormat.format("Person with UID {0} not found", id)));
    }

    @Override
    public Person create(final Person person) {
        inMemoryRepository.put(person.getUid(), person);
        return person;
    }

    @Override
    public Person save(final Person person) {
        Person p = inMemoryRepository.get(person.getUid());
        if (p == null) {
            this.create(person);
        } else {
            inMemoryRepository.replace(p.getUid(), person);
        }
        return person;
    }
}
