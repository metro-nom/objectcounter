package com.metronom.objectcounter;

import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;

/**
 * Counts the occurrences of certain criteria in a collection or stream of objects.
 * @author Thomas Stroeder
 * @param <T> The type of objects in the collection or stream.
 */
public abstract class ObjectCounter<T> {

    public static Optional<Long> sumOptionalCombiner(final Optional<Long> a, final Optional<Long> b) {
        if (a.isPresent() && b.isPresent()) {
            return Optional.of(a.get() + b.get());
        }
        return Optional.empty();
    }

    private final String description;

    /**
     * Creates a counter with the specified description.
     * @param description The description of this counter.
     */
    public ObjectCounter(final String description) {
        this.description = description;
    }

    /**
     * Counts the occurrences of certain criteria in a given object. By overriding this method, you can specify the
     * criteria to count.
     * @param object The object to count in.
     * @return The number of occurrences of certain criteria in the specified object. You can return an empty value to
     *         indicate that the collection of objects contains an illegal object for this count operation.
     */
    public abstract Optional<Long> count(T object);

    /**
     * @return The description of this counter.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * @see #getObjectCount(Stream)
     */
    public Optional<Long> getObjectCount(final Collection<T> objects) {
        return this.getObjectCount(objects.stream());
    }

    /**
     * @param objects The objects to count in.
     * @return The number of occurrences of the criteria specified in the {@link #count(Object)} method in the
     *         specified objects. An empty value indicates that the collection of objects contains an illegal object
     *         for this count operation.
     */
    public Optional<Long> getObjectCount(final Stream<T> objects) {
        this.init();
        return objects.reduce(Optional.of(0L), this::sumOptionalAccumulator, ObjectCounter::sumOptionalCombiner);
    }

    /**
     * @see #getObjectCountByDistinguisher(Function, Stream)
     */
    public <S> Map<S, Optional<Long>> getObjectCountByDistinguisher(
        final Function<T, S> distinguisher,
        final Collection<T> objects
    ) {
        return this.getObjectCountByDistinguisher(distinguisher, objects.stream());
    }

    /**
     * @param distinguisher A function yielding an object distinguishing the specified objects such that the count of
     *                      criteria is separate for each distinguishing object.
     * @param objects The objects to count in.
     * @return The number of occurrences of the criteria specified in the {@link #count(Object)} method in the
     *         specified objects separated for each distinguishing object according to the specified
     *         <code>distinguisher</code>. An empty value indicates that the collection of objects contains an illegal
     *         object for this count operation.
     */
    public <S> Map<S, Optional<Long>> getObjectCountByDistinguisher(
        final Function<T, S> distinguisher,
        final Stream<T> objects
    ) {
        return this.getObjectCountByMultiDistinguisher(o -> Collections.singleton(distinguisher.apply(o)), objects);
    }

    /**
     * @see #getObjectCountByMultiDistinguisher(Function, Stream)
     */
    public <S> Map<S, Optional<Long>> getObjectCountByMultiDistinguisher(
        final Function<T, Collection<S>> distinguisher,
        final Collection<T> objects
    ) {
        return this.getObjectCountByMultiDistinguisher(distinguisher, objects.stream());
    }

    /**
     * @param distinguisher A function yielding a collection of objects distinguishing the specified objects such that
     *                      the count of criteria is separate for each distinguishing object (but will be counted for
     *                      each distinguishing object in the collection).
     * @param objects The objects to count in.
     * @return The number of occurrences of the criteria specified in the {@link #count(Object)} method in the
     *         specified objects separated for each distinguishing object according to the specified
     *         <code>distinguisher</code>. An empty value indicates that the collection of objects contains an illegal
     *         object for this count operation.
     */
    public <S> Map<S, Optional<Long>> getObjectCountByMultiDistinguisher(
        final Function<T, Collection<S>> distinguisher,
        final Stream<T> objects
    ) {
        final Map<S, Optional<Long>> result = new LinkedHashMap<S, Optional<Long>>();
        this.init();
        objects.forEach(object -> {
            for (final S distinguished : distinguisher.apply(object)) {
                if (result.containsKey(distinguished)) {
                    result.put(
                        distinguished,
                        ObjectCounter.sumOptionalCombiner(result.get(distinguished), this.count(object))
                    );
                } else {
                    result.put(distinguished, this.count(object));
                }
            }
        });
        return result;
    }

    /**
     * A hook method being called whenever one of the counting methods is called. Can be used to clear any book-keeping
     * for a fresh counting.
     */
    public void init() {
        // DO NOTHING, JUST A HOOK FOR SUBCLASSES
    }

    private Optional<Long> sumOptionalAccumulator(final Optional<Long> sum, final T nextObject) {
        return ObjectCounter.sumOptionalCombiner(sum, this.count(nextObject));
    }

    @Override
    public String toString() {
        return this.description;
    }

}
