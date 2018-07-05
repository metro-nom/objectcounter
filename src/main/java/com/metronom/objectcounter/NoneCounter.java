package com.metronom.objectcounter;

/**
 * Does not count any object.
 * @author Thomas Stroeder
 * @param <T> The type of objects in the collection or stream.
 */
public class NoneCounter<T> extends ObjectCounter<T> {

    /**
     * Creates a NoneCounter with the specified description.
     * @param description The description of this counter.
     */
    public NoneCounter(final String description) {
        super(description);
    }

    @Override
    public long count(final T object) {
        return 0;
    }

}
