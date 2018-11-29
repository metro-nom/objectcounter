package com.metronom.objectcounter;

import java.util.*;

/**
 * Takes the maximum of the specified counters on the given objects.
 * @author Thomas Stroeder
 * @param <T> The type of objects in the collection or stream.
 */
public class MaxCounter<T> extends ObjectCounter<T> {

    private static Optional<Long> maxOptional(final Optional<Long> a, final Optional<Long> b) {
        if (a.isPresent() && b.isPresent()) {
            return Optional.of(Math.max(a.get(), b.get()));
        }
        return Optional.empty();
    }

    private final Collection<ObjectCounter<T>> counters;

    /**
     * @param description The description of this counter.
     * @param counters The counters to take the maximum from.
     */
    public MaxCounter(final String description, final Collection<ObjectCounter<T>> counters) {
        super(description);
        if (counters.isEmpty()) {
            throw new IllegalArgumentException("Maximum is not defined without any counter!");
        }
        this.counters = counters;
    }

    /**
     * @param description The description of this counter.
     * @param counters The counters to take the maximum from.
     */
    @SafeVarargs
    public MaxCounter(final String description, final ObjectCounter<T>... counters) {
        this(description, Arrays.asList(counters));
    }

    @Override
    public Optional<Long> count(final T object) {
        Optional<Long> result = this.counters.iterator().next().count(object);
        for (final ObjectCounter<T> counter : this.counters) {
            result = MaxCounter.maxOptional(result, counter.count(object));
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
