package poc.domain;

import java.io.Serializable;

public abstract class AggregateId<V, T> implements Serializable {
    private static final long serialVersionUID = -8023961600445571055L;

    public abstract V getValue();

    @Override
    public abstract String toString();
}
