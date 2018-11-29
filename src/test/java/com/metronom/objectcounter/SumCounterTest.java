package com.metronom.objectcounter;

import org.junit.*;
import org.testng.annotations.Test;

public class SumCounterTest {

    @Test
    public void sumsOne() {
        final ObjectCounter<String> counter = new SumCounter<String>("TestCounter", new AllCounter<String>("all"));
        Assert.assertEquals(1, counter.count("test").get().longValue());
    }

    @Test
    public void sumsZero() {
        final ObjectCounter<String> counter =
            new SumCounter<String>("TestCounter", new NoneCounter<String>("none1"), new NoneCounter<String>("none2"));
        Assert.assertEquals(0, counter.count("test").get().longValue());
    }

    @Test
    public void sumsTwo() {
        final ObjectCounter<String> counter =
            new SumCounter<String>("TestCounter", new AllCounter<String>("all1"), new AllCounter<String>("all2"));
        Assert.assertEquals(2, counter.count("test").get().longValue());
    }

}
