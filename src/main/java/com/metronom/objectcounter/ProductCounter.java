package com.metronom.objectcounter;

import java.util.*;

/**
 * Multiplies the specified counters on the given objects.
 * @author Thomas Stroeder
 * @param <T> The type of objects in the collection or stream.
 */
public class ProductCounter<T> extends ObjectCounter<T> {

    private static Optional<Long> multiplyOptional(final Optional<Long> a, final Optional<Long> b) {
        if (a.isPresent() && b.isPresent()) {
            return Optional.of(a.get() * b.get());
        }
        return Optional.empty();
    }

    private final Collection<ObjectCounter<T>> counters;

    /**
     * @param description The description of this counter.
     * @param counters The counters to multiply.
     */
    public ProductCounter(final String description, final Collection<ObjectCounter<T>> counters) {
        super(description);
        this.counters = counters;
    }

    /**
     * @param description The description of this counter.
     * @param counters The counters to multiply.
     */
    @SafeVarargs
    public ProductCounter(final String description, final ObjectCounter<T>... counters) {
        this(description, Arrays.asList(counters));
    }

    @Override
    public Optional<Long> count(final T object) {
        Optional<Long> result = Optional.of(1L);
        for (final ObjectCounter<T> counter : this.counters) {
            result = ProductCounter.multiplyOptional(result, counter.count(object));
        }
        return result;
    }

    @Override
    public void init() {
        for (final ObjectCounter<T> counter : this.counters) {
            counter.init();
        }
    }

}
