package poc.application.person.commands;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.Aggregate;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.commandhandling.model.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import poc.domain.person.Person;
import poc.domain.person.events.PersonCreated;
import poc.domain.person.events.PersonNameChanged;

@Component
public class PersonCommandHandler {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Repository<Person> axonRepo;

    // @Autowired
    // private Persons repository;

    @CommandHandler
    public void handle(final CreatePerson command) throws Exception {
        this.logger.info("Handling CreatePerson command");
        this.axonRepo.newInstance(() -> {
            AggregateLifecycle.apply(new PersonCreated(command.getPerson()));
            return command.getPerson();
        });
        // this.repository.create(command.getPerson());
    }

    @CommandHandler
    public void handle(final ChangePersonName command) {
        this.logger.info("Handling ChangePersonName command");
        Aggregate<Person> personAggregate = this.axonRepo.load(command.getUid().getValue());
        personAggregate.execute(
            person -> AggregateLifecycle.apply(new PersonNameChanged(command.getUid(), command.getName())));
    }
}
