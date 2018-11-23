package poc.application.person.events;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import poc.application.person.commands.exceptions.CannotChangeNameException;
import poc.application.person.queries.CountTotalPersonUpdated;
import poc.application.person.queries.CountTotalPersonsQuery;
import poc.application.person.queries.FindPersonById;
import poc.domain.person.Person;
import poc.domain.person.Persons;
import poc.domain.person.events.PersonCreated;
import poc.domain.person.events.PersonNameChanged;

// Not in domain layer because of spring annotation required
@Component
@ProcessingGroup("personsProcessor")
public class PersonProjection {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier("refog")
    Persons repository;

    @Autowired
    private QueryUpdateEmitter queryUpdateEmitter;

    @EventHandler
    protected void on(final PersonCreated event) {
        this.logger.info("Handling PersonCreated event for new refog");
        this.repository.add(event.getPerson());

        // TODO: used for subscription, find use cases
        this.queryUpdateEmitter.emit(CountTotalPersonsQuery.class, (query -> true), new CountTotalPersonUpdated());
    }

    @EventHandler
    protected void on(final PersonNameChanged event) {
        this.logger.info("Handling PersonNameChanged event for new refog");
        this.repository.changeName(event.getUid(), event.getName());
    }

    @EventHandler
    protected void on(final CannotChangeNameException exception) {
        this.logger
            .info("Handling CannotChangeNameException event for new refog, cause: " + exception.getMessage());
    }

    @QueryHandler
    protected Long handle(final CountTotalPersonsQuery query) {
        return this.repository.totalCount();
    }

    @QueryHandler
    protected Person handle(final FindPersonById query) {
        return this.repository.findById(query.getUid());
    }

}
