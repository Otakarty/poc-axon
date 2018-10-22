package poc.application.person.events;

import org.axonframework.commandhandling.model.Repository;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import poc.domain.person.Person;
import poc.domain.person.Persons;
import poc.domain.person.events.PersonCreated;
import poc.domain.person.events.PersonNameChanged;

// Not in domain layer because of spring annotation required
@Component
public class PersonEventListener {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    Persons repository;

    @Autowired
    private Repository<Person> axonRepo;

    @EventHandler
    protected void on(final PersonCreated event) {
        this.logger.info("Handling PersonCreated event");
        // throws exception if not found
        // Aggregate<Person> p = this.axonRepo.load(event.getUid().getValue());
        this.repository.save(event.getPerson());
    }

    @EventHandler
    protected void on(final PersonNameChanged event) {
        this.logger.info("Handling PersonNameChanged event");
        // throws exception if not found
        // Person p = this.repository.findById(event.getUid());
        // p.changeName(event.getName());
        this.axonRepo.load(event.getUid().getValue()).execute(person -> this.repository.save(person));
    }

}
