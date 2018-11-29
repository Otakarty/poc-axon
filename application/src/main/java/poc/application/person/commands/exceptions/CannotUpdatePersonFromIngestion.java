package poc.application.person.commands.exceptions;

import java.text.MessageFormat;

import org.axonframework.commandhandling.CommandExecutionException;

import poc.domain.person.IngestionPersonDpo;
import poc.domain.person.UID;

public class CannotUpdatePersonFromIngestion extends CommandExecutionException {
    private static final long serialVersionUID = 8704380766307047276L;

    public CannotUpdatePersonFromIngestion(final UID uid, final IngestionPersonDpo person, final String message,
        final Throwable cause) {
        super(MessageFormat.format("Cannot update person {0} from ingestion with payload {1}, cause: {2}",
            uid.getValue(), person.toString(), message), cause);
    }
}
