package poc.domain;

import java.io.Serializable;

public class UpdatableObject<T extends Serializable> implements Serializable {
    private static final long serialVersionUID = 3864576624793062284L;

    private final T object;

    public UpdatableObject(final T object) {
        this.object = object;
    }

    public final T get() {
        return this.object;
    }
}
