package poc.domain;

public abstract class AggregateId<V, T> {
    public abstract V getValue();

    @Override
    public abstract String toString();

    @SuppressWarnings("unchecked")
    public T cast() {
        return (T) this;
    };
}
