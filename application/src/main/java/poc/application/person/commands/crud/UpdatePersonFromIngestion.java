package poc.application.person.commands.crud;

import org.axonframework.commandhandling.CommandExecutionException;

import poc.application.commands.CommandInfo;
import poc.application.commands.UpdateCommand;
import poc.application.person.commands.exceptions.CannotUpdatePersonFromIngestion;
import poc.domain.events.DomainEvent;
import poc.domain.person.IngestionPersonDpo;
import poc.domain.person.Person;
import poc.domain.person.UID;
import poc.domain.person.events.PersonUpdatedFromIngestion;

public class UpdatePersonFromIngestion extends UpdateCommand<UID, Person> {
    private static final long serialVersionUID = 1261512518748775049L;
    private final IngestionPersonDpo newPersonPayload;

    public UpdatePersonFromIngestion(final CommandInfo commandInfo, final UID id,
        final IngestionPersonDpo newPersonPayload) {
        super(commandInfo, id);
        this.newPersonPayload = newPersonPayload;
    }

    @Override
    protected CommandExecutionException exceptionToThrow(final String causeMessage, final Throwable cause) {
        return new CannotUpdatePersonFromIngestion(this.getAggregateId(), this.newPersonPayload, causeMessage,
            cause);
    }

    @Override
    protected DomainEvent getDomainEvent() {
        return new PersonUpdatedFromIngestion(this.commandId, this.getAggregateId(), this.newPersonPayload);
    }

}
