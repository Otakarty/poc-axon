package poc.application.events;

import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import poc.domain.person.Persons;

@Component
public class OrderEventListener {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier("refog")
    Persons repository;

    @EventHandler
    protected void on(final OrderValidated event) {
        this.logger.info("Handling OrderValidated event for new refog");

        event.getCommandsToApply().forEach(command -> {
            try {
                command.applyToEventStore();
            } catch (Exception e) {
                // TODO : handle error
                e.printStackTrace();
            }
        });
    }

    @EventHandler
    protected void on(final InvalidOrderException exception) {
        this.logger.info("Handling InvalidOrderException for new refog, KO feedback to submit");
        this.logger.info("Commands in error: " + exception.getInErrorCommands());
    }
}
