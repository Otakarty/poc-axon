package poc.application.person.queries;

import poc.domain.person.UID;

public final class FindPersonById {
    private final UID uid;

    public FindPersonById(final UID uid) {
        this.uid = uid;
    }

    public UID getUid() {
        return this.uid;
    }
}
