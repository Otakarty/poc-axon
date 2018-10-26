package poc.synchro.old.refog;

import org.axonframework.commandhandling.model.Repository;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import poc.domain.person.Person;
import poc.domain.person.Persons;
import poc.domain.person.events.PersonCreated;
import poc.domain.person.events.PersonNameChanged;

@Component
public class PersonSynchroEventListener {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier("old-refog")
    Persons repository;

    @Autowired
    private Repository<Person> axonRepo;

    @EventHandler
    protected void on(final PersonCreated event) {
        this.logger.info("Handling PersonCreated event for old refog synchro");
        // throws exception if not found
        // Aggregate<Person> p = this.axonRepo.load(event.getUid().getValue());
        this.repository.save(event.getPerson());
    }

    @EventHandler
    protected void on(final PersonNameChanged event) {
        this.logger.info("Handling PersonNameChanged event for old refog synchro");
        // throws exception if not found
        // Person p = this.repository.findById(event.getUid());
        // p.changeName(event.getName());
        this.axonRepo.load(event.getUid().getValue()).execute(person -> this.repository.save(person));
    }

}
