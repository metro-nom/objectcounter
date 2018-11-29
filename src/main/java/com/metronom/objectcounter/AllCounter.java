package com.metronom.objectcounter;

import java.util.*;

/**
 * Counts each object once.
 * @author Thomas Stroeder
 * @param <T> The type of objects in the collection or stream.
 */
public class AllCounter<T> extends ObjectCounter<T> {

    /**
     * Creates an AllCounter with the specified description.
     * @param description The description of this counter.
     */
    public AllCounter(final String description) {
        super(description);
    }

    @Override
    public Optional<Long> count(final T object) {
        return Optional.of(1L);
    }

}
