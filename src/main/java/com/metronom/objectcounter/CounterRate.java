package com.metronom.objectcounter;

import java.util.*;
import java.util.function.*;

/**
 * Counts the total and actual occurrences of certain criteria in a collection or stream of objects and yields the
 * corresponding rate between these two amounts.
 * @author Thomas Stroeder
 * @param <T> The type of objects in the collection or stream.
 */
public class CounterRate<T> {

    private final ObjectCounter<T> count;

    private final String description;

    private final boolean percentage;

    private final ObjectCounter<T> total;

    /**
     * Creates a rate between the two specified counters.
     * @param description The description of this counter rate.
     * @param count A counter counting the actual amount.
     * @param total A counter counting the total amount.
     * @param percentage Flag indicating whether this rate should be given as a percentage.
     */
    public CounterRate(
        final String description,
        final ObjectCounter<T> count,
        final ObjectCounter<T> total,
        final boolean percentage
    ) {
        this.count = count;
        this.total = total;
        this.description = description;
        this.percentage = percentage;
    }

    /**
     * @return The description.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * @param objects The objects to count in.
     * @return The rate between the actual and total number of occurrences of certain criteria (specified by the given
     *         counters) in the specified objects. An empty value indicates that the collection of objects contains an
     *         illegal object for the underlying count operations.
     */
    public Optional<Double> getRate(final Collection<T> objects) {
        return this.getRate(this.count.getObjectCount(objects), this.total.getObjectCount(objects));
    }

    private Optional<Double> getRate(final Optional<Long> numCountOptional, final Optional<Long> numTotalOptional) {
        if (!numCountOptional.isPresent() || !numTotalOptional.isPresent()) {
            return Optional.empty();
        }
        final double numCount = numCountOptional.get();
        final double numTotal = numTotalOptional.get();
        return Optional.of(numCount * (this.isPercentage() ? 100.0 : 1.0) / numTotal);
    }

    /**
     * @param distinguisher A function yielding an object distinguishing the specified objects such that the count of
     *                      criteria is separate for each distinguishing object.
     * @param objects The objects to count in.
     * @return The rate between the actual and total number of occurrences of certain criteria (specified by the given
     *         counters) in the specified objects separated for each distinguishing object according to the specified
     *         <code>distinguisher</code>. An empty value indicates that the collection of objects contains an illegal
     *         object for the underlying count operations.
     */
    public <S> Map<S, Optional<Double>> getRateByDistinguisher(
        final Function<T, S> distinguisher,
        final Collection<T> objects
    ) {
        return this.getRateByMultiDistinguisher(o -> Collections.singleton(distinguisher.apply(o)), objects);
    }

    /**
     * @param distinguisher A function yielding a collection of objects distinguishing the specified objects such that
     *                      the count of criteria is separate for each distinguishing object (but will be counted for
     *                      each distinguishing object in the collection).
     * @param objects The objects to count in.
     * @return The rate between the actual and total number of occurrences of certain criteria (specified by the given
     *         counters) in the specified objects separated for each distinguishing object according to the specified
     *         <code>distinguisher</code>. An empty value indicates that the collection of objects contains an illegal
     *         object for the underlying count operations.
     */
    public <S> Map<S, Optional<Double>> getRateByMultiDistinguisher(
        final Function<T, Collection<S>> distinguisher,
        final Collection<T> objects
    ) {
        final Map<S, Optional<Double>> result = new LinkedHashMap<S, Optional<Double>>();
        final Map<S, Optional<Long>> countMap = this.count.getObjectCountByMultiDistinguisher(distinguisher, objects);
        final Map<S, Optional<Long>> totalMap = this.total.getObjectCountByMultiDistinguisher(distinguisher, objects);
        for (final Map.Entry<S, Optional<Long>> entry : totalMap.entrySet()) {
            final S distinguished = entry.getKey();
            final Optional<Long> numCount =
                countMap.containsKey(distinguished) ? countMap.get(distinguished) : Optional.of(0L);
            final Optional<Long> numTotal = entry.getValue();
            result.put(distinguished, this.getRate(numCount, numTotal));
        }
        return result;
    }

    /**
     * @return True iff this rate yields a percentage.
     */
    public boolean isPercentage() {
        return this.percentage;
    }

    @Override
    public String toString() {
        return this.description;
    }

}
