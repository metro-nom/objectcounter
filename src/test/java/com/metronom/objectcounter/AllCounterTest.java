package com.metronom.objectcounter;

import org.junit.*;
import org.testng.annotations.Test;

public class AllCounterTest {

    @Test
    public void countObject() {
        final ObjectCounter<String> counter = new AllCounter<String>("TestCounter");
        Assert.assertEquals(1, counter.count("test").get().longValue());
    }

    @Test
    public void countEmptyObject() {
        final ObjectCounter<String> counter = new AllCounter<String>("TestCounter");
        Assert.assertEquals(1, counter.count("").get().longValue());
    }

    @Test
    public void countNull() {
        final ObjectCounter<String> counter = new AllCounter<String>("TestCounter");
        Assert.assertEquals(1, counter.count(null).get().longValue());
    }

}
