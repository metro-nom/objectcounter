package com.metronom.objectcounter;

import java.util.*;

import org.junit.*;
import org.testng.annotations.Test;
import org.testng.collections.*;

public class ObjectCounterTest {

    boolean initCalled;
    boolean initCalledBefore;

    @Test
    public void actualCountOnNoIllegalObjects() {
        final String name = "TestCounter";
        final ObjectCounter<String> counter = new ObjectCounter<String>(name) {

            @Override
            public Optional<Long> count(final String object) {
                return "b".equals(object) ? Optional.empty() : Optional.of(1L);
            }

        };
        Assert.assertEquals(2, counter.getObjectCount(Lists.newArrayList("a", "c")).get().longValue());
    }

    @Test
    public void emptyCountOnOnlyIllegalObjects() {
        final String name = "TestCounter";
        final ObjectCounter<String> counter = new ObjectCounter<String>(name) {

            @Override
            public Optional<Long> count(final String object) {
                return Optional.empty();
            }

        };
        Assert.assertFalse(counter.getObjectCount(Lists.newArrayList("a", "b", "c")).isPresent());
    }

    @Test
    public void emptyCountOnSomeIllegalObject() {
        final String name = "TestCounter";
        final ObjectCounter<String> counter = new ObjectCounter<String>(name) {

            @Override
            public Optional<Long> count(final String object) {
                return "b".equals(object) ? Optional.empty() : Optional.of(1L);
            }

        };
        Assert.assertFalse(counter.getObjectCount(Lists.newArrayList("a", "b", "c")).isPresent());
    }

    @Test
    public void getObjectCountAllObjects() {
        final ObjectCounter<String> counter = new ObjectCounter<String>("TestCounter") {

            @Override
            public Optional<Long> count(final String object) {
                return Optional.of(1L);
            }

        };
        Assert.assertEquals(3, counter.getObjectCount(Lists.newArrayList("a", "b", "c")).get().longValue());
        Assert.assertEquals(3, counter.getObjectCount(Lists.newArrayList("a", "b", "c").stream()).get().longValue());
    }

    @Test
    public void getObjectCountBs() {
        final ObjectCounter<String> counter = new ObjectCounter<String>("TestCounter") {

            @Override
            public Optional<Long> count(final String object) {
                return Optional.of("b".equals(object) ? 1L : 0L);
            }

        };
        Assert.assertEquals(1, counter.getObjectCount(Lists.newArrayList("a", "b", "c")).get().longValue());
        Assert.assertEquals(1, counter.getObjectCount(Lists.newArrayList("a", "b", "c").stream()).get().longValue());
    }

    @Test
    public void getObjectCountByDistinguisherAllObjects() {
        final ObjectCounter<String> counter = new ObjectCounter<String>("TestCounter") {

            @Override
            public Optional<Long> count(final String object) {
                return Optional.of(1L);
            }

        };
        final Map<String, Optional<Long>> resultCollection =
            counter.getObjectCountByDistinguisher(string -> string, Lists.newArrayList("a", "b", "c"));
        Assert.assertEquals(3, resultCollection.size());
        Assert.assertEquals(Long.valueOf(1), resultCollection.get("a").get());
        Assert.assertEquals(Long.valueOf(1), resultCollection.get("b").get());
        Assert.assertEquals(Long.valueOf(1), resultCollection.get("c").get());
        final Map<String, Optional<Long>> resultStream =
            counter.getObjectCountByDistinguisher(string -> string, Lists.newArrayList("a", "b", "c").stream());
        Assert.assertEquals(3, resultStream.size());
        Assert.assertEquals(Long.valueOf(1), resultStream.get("a").get());
        Assert.assertEquals(Long.valueOf(1), resultStream.get("b").get());
        Assert.assertEquals(Long.valueOf(1), resultStream.get("c").get());
    }

    @Test
    public void getObjectCountByDistinguisherBs() {
        final ObjectCounter<String> counter = new ObjectCounter<String>("TestCounter") {

            @Override
            public Optional<Long> count(final String object) {
                return Optional.of("b".equals(object) ? 1L : 0L);
            }

        };
        final Map<String, Optional<Long>> resultCollection =
            counter.getObjectCountByDistinguisher(string -> string, Lists.newArrayList("a", "b", "c"));
        Assert.assertEquals(3, resultCollection.size());
        Assert.assertEquals(Long.valueOf(0), resultCollection.get("a").get());
        Assert.assertEquals(Long.valueOf(1), resultCollection.get("b").get());
        Assert.assertEquals(Long.valueOf(0), resultCollection.get("c").get());
        final Map<String, Optional<Long>> resultStream =
            counter.getObjectCountByDistinguisher(string -> string, Lists.newArrayList("a", "b", "c").stream());
        Assert.assertEquals(3, resultStream.size());
        Assert.assertEquals(Long.valueOf(0), resultStream.get("a").get());
        Assert.assertEquals(Long.valueOf(1), resultStream.get("b").get());
        Assert.assertEquals(Long.valueOf(0), resultStream.get("c").get());
    }

    @Test
    public void getObjectCountByDistinguisherWithoutDistinguishing() {
        final ObjectCounter<String> counter = new ObjectCounter<String>("TestCounter") {

            @Override
            public Optional<Long> count(final String object) {
                return Optional.of(1L);
            }

        };
        final Map<Integer, Optional<Long>> resultCollection =
            counter.getObjectCountByDistinguisher(string -> string.length(), Lists.newArrayList("a", "b", "c"));
        Assert.assertEquals(1, resultCollection.size());
        Assert.assertEquals(Long.valueOf(3), resultCollection.get(1).get());
        final Map<Integer, Optional<Long>> resultStream =
            counter.getObjectCountByDistinguisher(
                string -> string.length(),
                Lists.newArrayList("a", "b", "c").stream()
            );
        Assert.assertEquals(1, resultStream.size());
        Assert.assertEquals(Long.valueOf(3), resultStream.get(1).get());
    }

    @Test
    public void getObjectCountByDistinguisherZero() {
        final ObjectCounter<String> counter = new ObjectCounter<String>("TestCounter") {

            @Override
            public Optional<Long> count(final String object) {
                return Optional.of(0L);
            }

        };
        final Map<String, Optional<Long>> resultCollection =
            counter.getObjectCountByDistinguisher(string -> string, Lists.newArrayList("a", "b", "c"));
        Assert.assertEquals(3, resultCollection.size());
        Assert.assertEquals(Long.valueOf(0), resultCollection.get("a").get());
        Assert.assertEquals(Long.valueOf(0), resultCollection.get("b").get());
        Assert.assertEquals(Long.valueOf(0), resultCollection.get("c").get());
        final Map<String, Optional<Long>> resultStream =
            counter.getObjectCountByDistinguisher(string -> string, Lists.newArrayList("a", "b", "c").stream());
        Assert.assertEquals(3, resultStream.size());
        Assert.assertEquals(Long.valueOf(0), resultStream.get("a").get());
        Assert.assertEquals(Long.valueOf(0), resultStream.get("b").get());
        Assert.assertEquals(Long.valueOf(0), resultStream.get("c").get());
    }

    @Test
    public void getObjectCountByMultiDistinguisher() {
        final ObjectCounter<String> counter = new ObjectCounter<String>("TestCounter") {

            @Override
            public Optional<Long> count(final String object) {
                return Optional.of(1L);
            }

        };
        final Map<String, Optional<Long>> resultCollection =
            counter.getObjectCountByMultiDistinguisher(
                string -> Arrays.asList(string.split(",")),
                Lists.newArrayList("a,b", "b", "c")
            );
        Assert.assertEquals(3, resultCollection.size());
        Assert.assertEquals(Long.valueOf(1), resultCollection.get("a").get());
        Assert.assertEquals(Long.valueOf(2), resultCollection.get("b").get());
        Assert.assertEquals(Long.valueOf(1), resultCollection.get("c").get());
        final Map<String, Optional<Long>> resultStream =
            counter.getObjectCountByMultiDistinguisher(
                string -> Arrays.asList(string.split(",")),
                Lists.newArrayList("a,b", "b", "c").stream()
            );
        Assert.assertEquals(3, resultStream.size());
        Assert.assertEquals(Long.valueOf(1), resultStream.get("a").get());
        Assert.assertEquals(Long.valueOf(2), resultStream.get("b").get());
        Assert.assertEquals(Long.valueOf(1), resultStream.get("c").get());
    }

    @Test
    public void getObjectCountZero() {
        final ObjectCounter<String> counter = new ObjectCounter<String>("TestCounter") {

            @Override
            public Optional<Long> count(final String object) {
                return Optional.of(0L);
            }

        };
        Assert.assertEquals(0, counter.getObjectCount(Lists.newArrayList("a", "b", "c")).get().longValue());
        Assert.assertEquals(0, counter.getObjectCount(Lists.newArrayList("a", "b", "c").stream()).get().longValue());
    }

    @Test
    public void initIsBeingCalledBeforeCountInGetObjectCountByDistinguisherOnCollections() {
        this.initCalled = false;
        this.initCalledBefore = true;
        final ObjectCounter<String> counter = new ObjectCounter<String>("TestCounter") {

            @Override
            public Optional<Long> count(final String object) {
                ObjectCounterTest.this.initCalledBefore &=
                    ObjectCounterTest.this.initCalledBefore && ObjectCounterTest.this.initCalled;
                return Optional.of(0L);
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
            public Optional<Long> count(final String object) {
                ObjectCounterTest.this.initCalledBefore &=
                    ObjectCounterTest.this.initCalledBefore && ObjectCounterTest.this.initCalled;
                return Optional.of(0L);
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
            public Optional<Long> count(final String object) {
                ObjectCounterTest.this.initCalledBefore &=
                    ObjectCounterTest.this.initCalledBefore && ObjectCounterTest.this.initCalled;
                return Optional.of(0L);
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
            public Optional<Long> count(final String object) {
                ObjectCounterTest.this.initCalledBefore &=
                    ObjectCounterTest.this.initCalledBefore && ObjectCounterTest.this.initCalled;
                return Optional.of(0L);
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
            public Optional<Long> count(final String object) {
                ObjectCounterTest.this.initCalledBefore &=
                    ObjectCounterTest.this.initCalledBefore && ObjectCounterTest.this.initCalled;
                return Optional.of(0L);
            }

        };
        counter.getObjectCount(Lists.newArrayList("a", "b", "c"));
        Assert.assertFalse(this.initCalledBefore);
    }

    @Test
    public void toStringYieldsDescription() {
        final String name = "TestCounter";
        final ObjectCounter<String> counter = new ObjectCounter<String>(name) {

            @Override
            public Optional<Long> count(final String object) {
                return Optional.of(0L);
            }

        };
        Assert.assertEquals(name, counter.toString());
    }

}
