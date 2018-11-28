package poc.application.person.commands.crud;

import java.io.Serializable;

import org.junit.Assert;

public class NullableObject<T> implements Serializable {
    private static final long serialVersionUID = -3167592789286228615L;
    final Boolean isNull;
    final T object;

    private NullableObject(final T object) {
        Assert.assertFalse(object == null);
        this.isNull = false;
        this.object = object;
    }

    private NullableObject() {
        this.isNull = true;
        this.object = null;
    }

    public static <T> NullableObject<T> of(final T object) {
        return new NullableObject<>(object);
    }

    public static <T> NullableObject<T> empty() {
        return new NullableObject<>();
    }

}
