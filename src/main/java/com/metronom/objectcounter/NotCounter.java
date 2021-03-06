package com.metronom.objectcounter;

import java.util.*;

/**
 * Counts the current object once if the negated counter did not count anything in the current object. Otherwise it
 * does not count the current object.
 * @author Thomas Stroeder
 * @param <T> The type of objects in the collection or stream.
 */
public class NotCounter<T> extends ObjectCounter<T> {

    private final ObjectCounter<T> counter;

    /**
     * @param description The description of this counter.
     * @param counter The counter to negate.
     */
    public NotCounter(final String description, final ObjectCounter<T> counter) {
        super(description);
        this.counter = counter;
    }

    @Override
    public void init() {
        this.counter.init();
    }

    @Override
    public Optional<Long> count(final T object) {
        final Optional<Long> negatedResult = this.counter.count(object);
        if (negatedResult.isPresent()) {
            return Optional.of(negatedResult.get() > 0 ? 0L : 1L);
        }
        return Optional.empty();
    }

}
