package com.metronom.objectcounter;

import java.util.*;

import org.junit.*;
import org.testng.annotations.Test;
import org.testng.collections.*;

public class ObjectCounterTest {

    boolean initCalled;
    boolean initCalledBefore;

    @Test
    public void toStringYieldsDescription() {
        final String name = "TestCounter";
        final ObjectCounter<String> counter = new ObjectCounter<String>(name) {

            @Override
            public long count(final String object) {
                return 0;
            }

        };
        Assert.assertEquals(name, counter.toString());
    }

    @Test
    public void getObjectCountAllObjects() {
        final ObjectCounter<String> counter = new ObjectCounter<String>("TestCounter") {

            @Override
            public long count(final String object) {
                return 1;
            }

        };
        Assert.assertEquals(3, counter.getObjectCount(Lists.newArrayList("a", "b", "c")));
        Assert.assertEquals(3, counter.getObjectCount(Lists.newArrayList("a", "b", "c").stream()));
    }

    @Test
    public void getObjectCountBs() {
        final ObjectCounter<String> counter = new ObjectCounter<String>("TestCounter") {

            @Override
            public long count(final String object) {
                return "b".equals(object) ? 1 : 0;
            }

        };
        Assert.assertEquals(1, counter.getObjectCount(Lists.newArrayList("a", "b", "c")));
        Assert.assertEquals(1, counter.getObjectCount(Lists.newArrayList("a", "b", "c").stream()));
    }

    @Test
    public void getObjectCountByDistinguisherAllObjects() {
        final ObjectCounter<String> counter = new ObjectCounter<String>("TestCounter") {

            @Override
            public long count(final String object) {
                return 1;
            }

        };
        final Map<String, Long> resultCollection =
            counter.getObjectCountByDistinguisher(string -> string, Lists.newArrayList("a", "b", "c"));
        Assert.assertEquals(3, resultCollection.size());
        Assert.assertEquals(Long.valueOf(1), resultCollection.get("a"));
        Assert.assertEquals(Long.valueOf(1), resultCollection.get("b"));
        Assert.assertEquals(Long.valueOf(1), resultCollection.get("c"));
        final Map<String, Long> resultStream =
            counter.getObjectCountByDistinguisher(string -> string, Lists.newArrayList("a", "b", "c").stream());
        Assert.assertEquals(3, resultStream.size());
        Assert.assertEquals(Long.valueOf(1), resultStream.get("a"));
        Assert.assertEquals(Long.valueOf(1), resultStream.get("b"));
        Assert.assertEquals(Long.valueOf(1), resultStream.get("c"));
    }

    @Test
    public void getObjectCountByDistinguisherBs() {
        final ObjectCounter<String> counter = new ObjectCounter<String>("TestCounter") {

            @Override
            public long count(final String object) {
                return "b".equals(object) ? 1 : 0;
            }

        };
        final Map<String, Long> resultCollection =
            counter.getObjectCountByDistinguisher(string -> string, Lists.newArrayList("a", "b", "c"));
        Assert.assertEquals(3, resultCollection.size());
        Assert.assertEquals(Long.valueOf(0), resultCollection.get("a"));
        Assert.assertEquals(Long.valueOf(1), resultCollection.get("b"));
        Assert.assertEquals(Long.valueOf(0), resultCollection.get("c"));
        final Map<String, Long> resultStream =
            counter.getObjectCountByDistinguisher(string -> string, Lists.newArrayList("a", "b", "c").stream());
        Assert.assertEquals(3, resultStream.size());
        Assert.assertEquals(Long.valueOf(0), resultStream.get("a"));
        Assert.assertEquals(Long.valueOf(1), resultStream.get("b"));
        Assert.assertEquals(Long.valueOf(0), resultStream.get("c"));
    }

    @Test
    public void getObjectCountByDistinguisherWithoutDistinguishing() {
        final ObjectCounter<String> counter = new ObjectCounter<String>("TestCounter") {

            @Override
            public long count(final String object) {
                return 1;
            }

        };
        final Map<Integer, Long> resultCollection =
            counter.getObjectCountByDistinguisher(string -> string.length(), Lists.newArrayList("a", "b", "c"));
        Assert.assertEquals(1, resultCollection.size());
        Assert.assertEquals(Long.valueOf(3), resultCollection.get(1));
        final Map<Integer, Long> resultStream =
            counter.getObjectCountByDistinguisher(
                string -> string.length(),
                Lists.newArrayList("a", "b", "c").stream()
            );
        Assert.assertEquals(1, resultStream.size());
        Assert.assertEquals(Long.valueOf(3), resultStream.get(1));
    }

    @Test
    public void getObjectCountByDistinguisherZero() {
        final ObjectCounter<String> counter = new ObjectCounter<String>("TestCounter") {

            @Override
            public long count(final String object) {
                return 0;
            }

        };
        final Map<String, Long> resultCollection =
            counter.getObjectCountByDistinguisher(string -> string, Lists.newArrayList("a", "b", "c"));
        Assert.assertEquals(3, resultCollection.size());
        Assert.assertEquals(Long.valueOf(0), resultCollection.get("a"));
        Assert.assertEquals(Long.valueOf(0), resultCollection.get("b"));
        Assert.assertEquals(Long.valueOf(0), resultCollection.get("c"));
        final Map<String, Long> resultStream =
            counter.getObjectCountByDistinguisher(string -> string, Lists.newArrayList("a", "b", "c").stream());
        Assert.assertEquals(3, resultStream.size());
        Assert.assertEquals(Long.valueOf(0), resultStream.get("a"));
        Assert.assertEquals(Long.valueOf(0), resultStream.get("b"));
        Assert.assertEquals(Long.valueOf(0), resultStream.get("c"));
    }

    @Test
    public void getObjectCountZero() {
        final ObjectCounter<String> counter = new ObjectCounter<String>("TestCounter") {

            @Override
            public long count(final String object) {
                return 0;
            }

        };
        Assert.assertEquals(0, counter.getObjectCount(Lists.newArrayList("a", "b", "c")));
        Assert.assertEquals(0, counter.getObjectCount(Lists.newArrayList("a", "b", "c").stream()));
    }

    @Test
    public void initIsBeingCalledBeforeCountInGetObjectCountByDistinguisherOnCollections() {
        this.initCalled = false;
        this.initCalledBefore = true;
        final ObjectCounter<String> counter = new ObjectCounter<String>("TestCounter") {

            @Override
            public long count(final String object) {
                ObjectCounterTest.this.initCalledBefore &=
                    ObjectCounterTest.this.initCalledBefore && ObjectCounterTest.this.initCalled;
                return 0;
            }

            @Override
            public void init() {
                ObjectCounterTest.this.initCalled = true;
            }

        };
        counter.getObjectCountByDistinguisher(string -> string.charAt(0), Lists.newArrayList("a", "b", "c"));
        Assert.assertTrue(this.initCalledBefore);
    }

    @Test
    public void initIsBeingCalledBeforeCountInGetObjectCountByDistinguisherOnStreams() {
        this.initCalled = false;
        this.initCalledBefore = true;
        final ObjectCounter<String> counter = new ObjectCounter<String>("TestCounter") {

            @Override
            public long count(final String object) {
                ObjectCounterTest.this.initCalledBefore &=
                    ObjectCounterTest.this.initCalledBefore && ObjectCounterTest.this.initCalled;
                return 0;
            }

            @Override
            public void init() {
                ObjectCounterTest.this.initCalled = true;
            }

        };
        counter.getObjectCountByDistinguisher(string -> string.charAt(0), Lists.newArrayList("a", "b", "c").stream());
        Assert.assertTrue(this.initCalledBefore);
    }

    @Test
    public void initIsBeingCalledBeforeCountInGetObjectCountOnCollections() {
        this.initCalled = false;
        this.initCalledBefore = true;
        final ObjectCounter<String> counter = new ObjectCounter<String>("TestCounter") {

            @Override
            public long count(final String object) {
                ObjectCounterTest.this.initCalledBefore &=
                    ObjectCounterTest.this.initCalledBefore && ObjectCounterTest.this.initCalled;
                return 0;
            }

            @Override
            public void init() {
                ObjectCounterTest.this.initCalled = true;
            }

        };
        counter.getObjectCount(Lists.newArrayList("a", "b", "c"));
        Assert.assertTrue(this.initCalledBefore);
    }

    @Test
    public void initIsBeingCalledBeforeCountInGetObjectCountOnStreams() {
        this.initCalled = false;
        this.initCalledBefore = true;
        final ObjectCounter<String> counter = new ObjectCounter<String>("TestCounter") {

            @Override
            public long count(final String object) {
                ObjectCounterTest.this.initCalledBefore &=
                    ObjectCounterTest.this.initCalledBefore && ObjectCounterTest.this.initCalled;
                return 0;
            }

            @Override
            public void init() {
                ObjectCounterTest.this.initCalled = true;
            }

        };
        counter.getObjectCount(Lists.newArrayList("a", "b", "c").stream());
        Assert.assertTrue(this.initCalledBefore);
    }

    @Test
    public void testSetupForInitWorks() {
        this.initCalled = false;
        this.initCalledBefore = true;
        final ObjectCounter<String> counter = new ObjectCounter<String>("TestCounter") {

            @Override
            public long count(final String object) {
                ObjectCounterTest.this.initCalledBefore &=
                    ObjectCounterTest.this.initCalledBefore && ObjectCounterTest.this.initCalled;
                return 0;
            }

        };
        counter.getObjectCount(Lists.newArrayList("a", "b", "c"));
        Assert.assertFalse(this.initCalledBefore);
    }

}
