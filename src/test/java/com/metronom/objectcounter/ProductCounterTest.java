package com.metronom.objectcounter;

import org.junit.*;
import org.testng.annotations.Test;

public class ProductCounterTest {

    @Test
    public void yieldsOne() {
        final ObjectCounter<String> counter = new ProductCounter<String>("TestCounter", new AllCounter<String>("all"));
        Assert.assertEquals(1, counter.count("test"));
    }

    @Test
    public void yieldsZero() {
        final ObjectCounter<String> counter =
            new ProductCounter<String>("TestCounter", new NoneCounter<String>("none"));
        Assert.assertEquals(0, counter.count("test"));
    }

    @Test
    public void yieldsOneForTwoCounters() {
        final ObjectCounter<String> counter =
            new ProductCounter<String>("TestCounter", new AllCounter<String>("all1"), new AllCounter<String>("all2"));
        Assert.assertEquals(1, counter.count("test"));
    }

    @Test
    public void yieldsZeroForTwoCounters() {
        final ObjectCounter<String> counter =
            new ProductCounter<String>("TestCounter", new AllCounter<String>("all"), new NoneCounter<String>("none"));
        Assert.assertEquals(0, counter.count("test"));
    }

}
