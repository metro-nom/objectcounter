package com.metronom.objectcounter;

import org.junit.*;
import org.testng.annotations.Test;

public class NoneCounterTest {

    @Test
    public void dontCountObject() {
        final ObjectCounter<String> counter = new NoneCounter<String>("TestCounter");
        Assert.assertEquals(0, counter.count("test"));
    }

    @Test
    public void dontCountEmptyObject() {
        final ObjectCounter<String> counter = new NoneCounter<String>("TestCounter");
        Assert.assertEquals(0, counter.count(""));
    }

    @Test
    public void dontCountNull() {
        final ObjectCounter<String> counter = new NoneCounter<String>("TestCounter");
        Assert.assertEquals(0, counter.count(null));
    }

}
