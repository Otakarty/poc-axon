package poc.synchro.old.refog;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier("old-refog")
public class PersonsSynchroRepository /* implements Persons */ {

    // private static Map<UID, Person> inMemoryRepository = new HashMap<>();
    //
    // @Override
    // public Person findById(final UID id) {
    // return Optional.ofNullable(inMemoryRepository.get(id)).orElseThrow(
    // () -> new IllegalArgumentException(MessageFormat.format("Person with UID {0} not found", id)));
    // }
    //
    // @Override
    // public Person create(final Person person) {
    // inMemoryRepository.put(person.getUid(), person);
    // return person;
    // }
    //
    // @Override
    // public Person save(final Person person) {
    // Person p = inMemoryRepository.get(person.getUid());
    // if (p == null) {
    // this.create(person);
    // } else {
    // inMemoryRepository.replace(p.getUid(), person);
    // }
    // return person;
    // }
    //
    // @Override
    // public List<Person> findAll() {
    // return inMemoryRepository.values().stream().collect(Collectors.toList());
    // }
}
