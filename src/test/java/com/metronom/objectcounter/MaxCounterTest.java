package com.metronom.objectcounter;

import org.junit.*;
import org.testng.annotations.Test;

public class MaxCounterTest {

    @Test
    public void yieldsOne() {
        final ObjectCounter<String> counter = new MaxCounter<String>("TestCounter", new AllCounter<String>("all"));
        Assert.assertEquals(1, counter.count("test").get().longValue());
    }

    @Test
    public void yieldsOneForTwoCounters() {
        final ObjectCounter<String> counter =
            new MaxCounter<String>("TestCounter", new NoneCounter<String>("none"), new AllCounter<String>("all"));
        Assert.assertEquals(1, counter.count("test").get().longValue());
    }

    @Test
    public void yieldsZeroForTwoCounters() {
        final ObjectCounter<String> counter =
            new MaxCounter<String>("TestCounter", new NoneCounter<String>("none1"), new NoneCounter<String>("none2"));
        Assert.assertEquals(0, counter.count("test").get().longValue());
    }

}
