package poc.domain.exceptions;

import java.text.MessageFormat;

public class WhiteEventException extends RuntimeException {
    private static final long serialVersionUID = 1210476562238655136L;

    public WhiteEventException(final String cause) {
        super(MessageFormat.format("Nothing happened, cause: {0}", cause));
    }
}
