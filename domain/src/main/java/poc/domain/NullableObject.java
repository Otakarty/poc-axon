package poc.domain;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class NullableObject<T extends Serializable> implements Serializable {
    private static final long serialVersionUID = 4725948540136374870L;

    /**
     * Common instance for {@code empty()}.
     */
    private static final NullableObject<?> EMPTY = new NullableObject<>();

    /**
     * If non-null, the value; if null, indicates no value is present
     */
    private final T value;

    /**
     * Constructs an empty instance.
     * @implNote Generally only one empty instance, {@link NullableObject#EMPTY}, should exist per VM.
     */
    private NullableObject() {
        this.value = null;
    }

    /**
     * Returns an empty {@code NullableObject} instance. No value is present for this NullableObject.
     * @apiNote Though it may be tempting to do so, avoid testing if an object is empty by comparing with
     *          {@code ==} against instances returned by {@code Option.empty()}. There is no guarantee that it is a
     *          singleton. Instead, use {@link #isPresent()}.
     * @param <T> Type of the non-existent value
     * @return an empty {@code NullableObject}
     */
    public static <T extends Serializable> NullableObject<T> empty() {
        @SuppressWarnings("unchecked")
        NullableObject<T> t = (NullableObject<T>) EMPTY;
        return t;
    }

    /**
     * Constructs an instance with the value present.
     * @param value the non-null value to be present
     * @throws NullPointerException if value is null
     */
    private NullableObject(final T value) {
        this.value = Objects.requireNonNull(value);
    }

    /**
     * Returns an {@code NullableObject} with the specified present non-null value.
     * @param <T> the class of the value
     * @param value the value to be present, which must be non-null
     * @return an {@code NullableObject} with the value present
     * @throws NullPointerException if value is null
     */
    public static <T extends Serializable> NullableObject<T> of(final T value) {
        return new NullableObject<>(value);
    }

    /**
     * Returns an {@code NullableObject} describing the specified value, if non-null, otherwise returns an empty
     * {@code NullableObject}.
     * @param <T> the class of the value
     * @param value the possibly-null value to describe
     * @return an {@code NullableObject} with a present value if the specified value is non-null, otherwise an
     *         empty {@code NullableObject}
     */
    public static <T extends Serializable> NullableObject<T> ofNullable(final T value) {
        return value == null ? empty() : of(value);
    }

    /**
     * If a value is present in this {@code NullableObject}, returns the value, otherwise throws
     * {@code NoSuchElementException}.
     * @return the non-null value held by this {@code NullableObject}
     * @throws NoSuchElementException if there is no value present
     * @see NullableObject#isPresent()
     */
    public T get() {
        if (this.value == null) {
            throw new NoSuchElementException("No value present");
        }
        return this.value;
    }

    /**
     * Return {@code true} if there is a value present, otherwise {@code false}.
     * @return {@code true} if there is a value present, otherwise {@code false}
     */
    public boolean isPresent() {
        return this.value != null;
    }

    /**
     * If a value is present, invoke the specified consumer with the value, otherwise do nothing.
     * @param consumer block to be executed if a value is present
     * @throws NullPointerException if value is present and {@code consumer} is null
     */
    public void ifPresent(final Consumer<? super T> consumer) {
        if (this.value != null) {
            consumer.accept(this.value);
        }
    }

    /**
     * If a value is present, and the value matches the given predicate, return an {@code NullableObject}
     * describing the value, otherwise return an empty {@code NullableObject}.
     * @param predicate a predicate to apply to the value, if present
     * @return an {@code NullableObject} describing the value of this {@code NullableObject} if a value is present
     *         and the value matches the given predicate, otherwise an empty {@code NullableObject}
     * @throws NullPointerException if the predicate is null
     */
    public NullableObject<T> filter(final Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        if (!this.isPresent()) {
            return this;
        } else {
            return predicate.test(this.value) ? this : empty();
        }
    }

    /**
     * If a value is present, apply the provided mapping function to it, and if the result is non-null, return an
     * {@code NullableObject} describing the result. Otherwise return an empty {@code NullableObject}.
     * @apiNote This method supports post-processing on optional values, without the need to explicitly check for a
     *          return status. For example, the following code traverses a stream of file names, selects one that
     *          has not yet been processed, and then opens that file, returning an
     *          {@code NullableObject<FileInputStream>}:
     *
     *          <pre>
     * {@code
     *     NullableObject<FileInputStream> fis =
     *         names.stream().filter(name -> !isProcessedYet(name))
     *                       .findFirst()
     *                       .map(name -> new FileInputStream(name));
     * }
     *          </pre>
     *
     *          Here, {@code findFirst} returns an {@code NullableObject<String>}, and then {@code map} returns an
     *          {@code NullableObject<FileInputStream>} for the desired file if one exists.
     * @param <U> The type of the result of the mapping function
     * @param mapper a mapping function to apply to the value, if present
     * @return an {@code NullableObject} describing the result of applying a mapping function to the value of this
     *         {@code NullableObject}, if a value is present, otherwise an empty {@code NullableObject}
     * @throws NullPointerException if the mapping function is null
     */
    public <U extends Serializable> NullableObject<U> map(final Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        if (!this.isPresent()) {
            return empty();
        } else {
            return NullableObject.ofNullable(mapper.apply(this.value));
        }
    }

    /**
     * If a value is present, apply the provided {@code NullableObject}-bearing mapping function to it, return that
     * result, otherwise return an empty {@code NullableObject}. This method is similar to {@link #map(Function)},
     * but the provided mapper is one whose result is already an {@code NullableObject}, and if invoked,
     * {@code flatMap} does not wrap it with an additional {@code NullableObject}.
     * @param <U> The type parameter to the {@code NullableObject} returned by
     * @param mapper a mapping function to apply to the value, if present the mapping function
     * @return the result of applying an {@code NullableObject}-bearing mapping function to the value of this
     *         {@code NullableObject}, if a value is present, otherwise an empty {@code NullableObject}
     * @throws NullPointerException if the mapping function is null or returns a null result
     */
    public <U extends Serializable> NullableObject<U> flatMap(
        final Function<? super T, NullableObject<U>> mapper) {
        Objects.requireNonNull(mapper);
        if (!this.isPresent()) {
            return empty();
        } else {
            return Objects.requireNonNull(mapper.apply(this.value));
        }
    }

    /**
     * Return the value if present, otherwise return {@code other}.
     * @param other the value to be returned if there is no value present, may be null
     * @return the value, if present, otherwise {@code other}
     */
    public T orElse(final T other) {
        return this.value != null ? this.value : other;
    }

    /**
     * Return the value if present, otherwise invoke {@code other} and return the result of that invocation.
     * @param other a {@code Supplier} whose result is returned if no value is present
     * @return the value if present otherwise the result of {@code other.get()}
     * @throws NullPointerException if value is not present and {@code other} is null
     */
    public T orElseGet(final Supplier<? extends T> other) {
        return this.value != null ? this.value : other.get();
    }

    /**
     * Return the contained value, if present, otherwise throw an exception to be created by the provided supplier.
     * @apiNote A method reference to the exception constructor with an empty argument list can be used as the
     *          supplier. For example, {@code IllegalStateException::new}
     * @param <X> Type of the exception to be thrown
     * @param exceptionSupplier The supplier which will return the exception to be thrown
     * @return the present value
     * @throws X if there is no value present
     * @throws NullPointerException if no value is present and {@code exceptionSupplier} is null
     */
    public <X extends Throwable> T orElseThrow(final Supplier<? extends X> exceptionSupplier) throws X {
        if (this.value != null) {
            return this.value;
        } else {
            throw exceptionSupplier.get();
        }
    }

    /**
     * Indicates whether some other object is "equal to" this NullableObject. The other object is considered equal
     * if:
     * <ul>
     * <li>it is also an {@code NullableObject} and;
     * <li>both instances have no value present or;
     * <li>the present values are "equal to" each other via {@code equals()}.
     * </ul>
     * @param obj an object to be tested for equality
     * @return {code true} if the other object is "equal to" this object otherwise {@code false}
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof NullableObject)) {
            return false;
        }

        NullableObject<?> other = (NullableObject<?>) obj;
        return Objects.equals(this.value, other.value);
    }

    /**
     * Returns the hash code value of the present value, if any, or 0 (zero) if no value is present.
     * @return hash code value of the present value or 0 if no value is present
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(this.value);
    }

    /**
     * Returns a non-empty string representation of this NullableObject suitable for debugging. The exact
     * presentation format is unspecified and may vary between implementations and versions.
     * @implSpec If a value is present the result must include its string representation in the result. Empty and
     *           present NullableObjects must be unambiguously differentiable.
     * @return the string representation of this instance
     */
    @Override
    public String toString() {
        return this.value != null ? String.format("NullableObject[%s]", this.value) : "NullableObject.empty";
    }

}
