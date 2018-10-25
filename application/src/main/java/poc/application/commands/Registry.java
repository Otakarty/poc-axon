package poc.application.commands;

import org.axonframework.commandhandling.model.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import poc.domain.person.Person;

@Component
public class Registry {

    private static Repository<Person> personRepository;

    public static <T> Repository<T> getRepository(final Class<T> aggregate) {
        Repository<T> repository = null;

        if (Person.class.equals(aggregate)) {
            repository = (Repository<T>) personRepository;
        } else {
            throw new UnsupportedOperationException("Aggregate not yet supported");
        }
        return repository;
    }

    @Autowired
    public void setPersonRepository(final Repository<Person> personRepository) {
        Registry.personRepository = personRepository;
    }
}
