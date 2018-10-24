package poc.application.events;

import org.axonframework.commandhandling.model.Aggregate;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.commandhandling.model.Repository;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import poc.domain.person.Person;
import poc.domain.person.Persons;

// Not in domain layer because of spring annotation required
@Component
public class OrderEventListener {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier("refog")
    Persons repository;

    @Autowired
    private Repository<Person> axonRepo;

    @EventHandler
    protected void on(final OrderValidated event) {
        this.logger.info("Handling OrderValidated event for new refog");
        this.logger.info("Domain events to apply: " + event.getEventsToApply());

        Aggregate<Person> personAggregate = this.axonRepo.load(event.getId().getValue());
        personAggregate.execute(person -> event.getEventsToApply().stream().forEach(eventToApply -> {
            AggregateLifecycle.apply(eventToApply);
        }));
    }

    @EventHandler
    protected void on(final InvalidOrderException exception) {
        this.logger.info("Handling InvalidOrderException for new refog, KO feedback to submit");
    }
}
