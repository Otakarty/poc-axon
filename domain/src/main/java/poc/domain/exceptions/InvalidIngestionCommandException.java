package poc.domain.exceptions;

import java.util.List;

public class InvalidIngestionCommandException extends Exception {
    private static final long serialVersionUID = 1907822137794645706L;
    private final List<IllegalArgumentException> exceptions;

    public InvalidIngestionCommandException(final List<IllegalArgumentException> exceptions) {
        super("Invalid ingestion command, cause(s): " + exceptions);
        this.exceptions = exceptions;
    }

    public final List<IllegalArgumentException> getExceptions() {
        return this.exceptions;
    }
}
