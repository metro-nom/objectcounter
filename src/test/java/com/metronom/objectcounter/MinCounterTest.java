package com.metronom.objectcounter;

import org.junit.*;
import org.testng.annotations.Test;

public class MinCounterTest {

    @Test
    public void yieldsOne() {
        final ObjectCounter<String> counter = new MinCounter<String>("TestCounter", new AllCounter<String>("all"));
        Assert.assertEquals(1, counter.count("test").get().longValue());
    }

    @Test
    public void yieldsOneForTwoCounters() {
        final ObjectCounter<String> counter =
            new MinCounter<String>("TestCounter", new AllCounter<String>("all1"), new AllCounter<String>("all2"));
        Assert.assertEquals(1, counter.count("test").get().longValue());
    }

    @Test
    public void yieldsZeroForTwoCounters() {
        final ObjectCounter<String> counter =
            new MinCounter<String>("TestCounter", new AllCounter<String>("all"), new NoneCounter<String>("none"));
        Assert.assertEquals(0, counter.count("test").get().longValue());
    }

}
