package poc.application.commands;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.model.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import poc.domain.person.Person;
import poc.infrastructure.CommandJpaRepository;

@Component
public class Registry {

    private static Repository<Person> personRepository;
    private static CommandJpaRepository commandRepository;
    private static CommandGateway commandGateway;

    public static <T> Repository<T> getRepository(final Class<T> aggregate) {
        Repository<T> repository = null;

        if (Person.class.equals(aggregate)) {
            repository = (Repository<T>) personRepository;
        } else {
            throw new UnsupportedOperationException("Aggregate not yet supported");
        }
        return repository;
    }

    public static CommandJpaRepository getCommandRepository() {
        return commandRepository;
    }

    public static CommandGateway getCommandGateway() {
        return commandGateway;
    }

    @Autowired
    public void setPersonRepository(final Repository<Person> personRepository) {
        Registry.personRepository = personRepository;
    }

    @Autowired
    public void setCommandRepository(final CommandJpaRepository commandRepository) {
        Registry.commandRepository = commandRepository;
    }

    @Autowired
    public void setCommandGateway(final CommandGateway commandGateway) {
        Registry.commandGateway = commandGateway;
    }

    public static Repository<Person> getPersonRepository() {
        return personRepository;
    }

}
