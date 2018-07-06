package com.metronom.objectcounter;

import java.util.*;

/**
 * Multiplies the specified counters on the given objects.
 * @author Thomas Stroeder
 * @param <T> The type of objects in the collection or stream.
 */
public class ProductCounter<T> extends ObjectCounter<T> {

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
    public long count(final T object) {
        long result = 1;
        for (final ObjectCounter<T> counter : this.counters) {
            result *= counter.count(object);
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
