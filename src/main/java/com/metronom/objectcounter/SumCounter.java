package com.metronom.objectcounter;

import java.util.*;

/**
 * Sums the specified counters on the given objects.
 * @author Thomas Stroeder
 * @param <T> The type of objects in the collection or stream.
 */
public class SumCounter<T> extends ObjectCounter<T> {

    private final Collection<ObjectCounter<T>> counters;

    /**
     * @param description The description of this counter.
     * @param counters The counters to sum.
     */
    public SumCounter(final String description, final Collection<ObjectCounter<T>> counters) {
        super(description);
        this.counters = counters;
    }

    /**
     * @param description The description of this counter.
     * @param counters The counters to sum.
     */
    @SafeVarargs
    public SumCounter(final String description, final ObjectCounter<T>... counters) {
        this(description, Arrays.asList(counters));
    }

    @Override
    public long count(final T object) {
        long result = 0;
        for (final ObjectCounter<T> counter : this.counters) {
            result += counter.count(object);
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
