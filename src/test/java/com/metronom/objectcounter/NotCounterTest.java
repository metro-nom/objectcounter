package com.metronom.objectcounter;

import org.junit.*;
import org.testng.annotations.Test;

public class NotCounterTest {

    @Test
    public void dontCountObject() {
        final ObjectCounter<String> counter = new NotCounter<String>("TestCounter", new AllCounter<String>("All"));
        Assert.assertEquals(0, counter.count("test").get().longValue());
    }

    @Test
    public void dontCountEmptyObject() {
        final ObjectCounter<String> counter = new NotCounter<String>("TestCounter", new AllCounter<String>("All"));
        Assert.assertEquals(0, counter.count("").get().longValue());
    }

    @Test
    public void dontCountNull() {
        final ObjectCounter<String> counter = new NotCounter<String>("TestCounter", new AllCounter<String>("All"));
        Assert.assertEquals(0, counter.count(null).get().longValue());
    }

    @Test
    public void countObject() {
        final ObjectCounter<String> counter = new NotCounter<String>("TestCounter", new NoneCounter<String>("None"));
        Assert.assertEquals(1, counter.count("test").get().longValue());
    }

    @Test
    public void countEmptyObject() {
        final ObjectCounter<String> counter = new NotCounter<String>("TestCounter", new NoneCounter<String>("None"));
        Assert.assertEquals(1, counter.count("").get().longValue());
    }

    @Test
    public void countNull() {
        final ObjectCounter<String> counter = new NotCounter<String>("TestCounter", new NoneCounter<String>("None"));
        Assert.assertEquals(1, counter.count(null).get().longValue());
    }

}
