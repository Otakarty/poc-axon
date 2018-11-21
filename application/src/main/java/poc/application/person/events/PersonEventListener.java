package poc.application.person.events;

import org.axonframework.commandhandling.model.Repository;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import poc.application.person.commands.exceptions.CannotChangeNameException;
import poc.domain.person.Person;
import poc.domain.person.Persons;
import poc.domain.person.events.PersonCreated;
import poc.domain.person.events.PersonNameChanged;
import poc.infrastructure.CommandRepository;

// Not in domain layer because of spring annotation required
@Component
public class PersonEventListener {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier("refog")
    Persons repository;

    @Autowired
    private Repository<Person> axonRepo;

    // @Autowired
    // private CommandJpaRepository commandRepository;

    @Autowired
    private CommandRepository commandRepository;

    @EventHandler
    protected void on(final PersonCreated event) {
        this.logger.info("Handling PersonCreated event for new refog");
        // throws exception if not found
        // Aggregate<Person> p = this.axonRepo.load(event.getUid().getValue());
        this.repository.save(event.getPerson());

        // // TODO: try to put this code on a generic event handler
        // this.commandRepository.finishCommand(event.getCommandId().toString());
    }

    @EventHandler
    protected void on(final PersonNameChanged event) {
        this.logger.info("Handling PersonNameChanged event for new refog");
        // throws exception if not found
        // Person p = this.repository.findById(event.getUid());
        // p.changeName(event.getName());
        this.axonRepo.load(event.getUid().getValue()).execute(person -> this.repository.save(person));

        // // TODO: try to put this code on a generic event handler
        // this.commandRepository.finishCommand(event.getCommandId().toString());
    }

    @EventHandler
    protected void on(final CannotChangeNameException exception) {
        this.logger
            .info("Handling CannotChangeNameException event for new refog, cause: " + exception.getMessage());
    }

}
