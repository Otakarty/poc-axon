package poc.application.person;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import poc.application.person.commands.ChangePersonName;
import poc.application.person.commands.CreatePerson;
import poc.domain.person.Name;
import poc.domain.person.Person;
import poc.domain.person.Persons;
import poc.domain.person.UID;

@Service
public class PersonService {
    @Autowired
    private CommandGateway commandGateway;

    @Autowired
    private EventStore eventStore;

    @Autowired
    @Qualifier("refog")
    private Persons repository;

    public void createPerson(final Person person) {
        this.commandGateway.send(new CreatePerson(person));
    }

    public void changePersonName(final UID uid, final Name newName) {
        this.commandGateway.send(new ChangePersonName(uid, newName));
    }

    // TODO: read model
    public Person getPersonFromEvents(final UID uid) {
        this.eventStore.readEvents(uid.getValue()).asStream().forEach(event -> {
            System.out.println(event);
        });
        return null;
    }

    public PersonDTO getPersonSnapshot(final UID uid) {
        return new PersonDTO(this.repository.findById(uid));
    }

}
