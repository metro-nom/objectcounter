package com.metronom.objectcounter;

import java.util.*;

/**
 * Takes the minimum of the specified counters on the given objects.
 * @author Thomas Stroeder
 * @param <T> The type of objects in the collection or stream.
 */
public class MinCounter<T> extends ObjectCounter<T> {

    private static Optional<Long> minOptional(final Optional<Long> a, final Optional<Long> b) {
        if (a.isPresent() && b.isPresent()) {
            return Optional.of(Math.min(a.get(), b.get()));
        }
        return Optional.empty();
    }

    private final Collection<ObjectCounter<T>> counters;

    /**
     * @param description The description of this counter.
     * @param counters The counters to take the minimum from.
     */
    public MinCounter(final String description, final Collection<ObjectCounter<T>> counters) {
        super(description);
        if (counters.isEmpty()) {
            throw new IllegalArgumentException("Minimum is not defined without any counter!");
        }
        this.counters = counters;
    }

    /**
     * @param description The description of this counter.
     * @param counters The counters to take the minimum from.
     */
    @SafeVarargs
    public MinCounter(final String description, final ObjectCounter<T>... counters) {
        this(description, Arrays.asList(counters));
    }

    @Override
    public Optional<Long> count(final T object) {
        Optional<Long> result = this.counters.iterator().next().count(object);
        for (final ObjectCounter<T> counter : this.counters) {
            result = MinCounter.minOptional(result, counter.count(object));
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
