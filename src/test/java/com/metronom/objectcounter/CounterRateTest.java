package com.metronom.objectcounter;

import java.util.*;

import org.junit.*;
import org.testng.annotations.Test;
import org.testng.collections.*;

public class CounterRateTest {

    @Test
    public void getRate25Percent() {
        final CounterRate<String> rate =
            new CounterRate<String>(
                "TestRate",
                new ObjectCounter<String>("Bs") {

                    @Override
                    public long count(final String object) {
                        return "b".equals(object) ? 1: 0;
                    }

                },
                new AllCounter<String>("all"),
                true
            );
        Assert.assertEquals(25.0, rate.getRate(Lists.newArrayList("a", "b", "c", "d")), Double.MIN_NORMAL);
    }

    @Test
    public void getRate25PercentageByDistinguisher() {
        final CounterRate<String> rate =
            new CounterRate<String>(
                "TestRate",
                new ObjectCounter<String>("Bs") {

                    @Override
                    public long count(final String object) {
                        return "b".equals(object) ? 1: 0;
                    }

                },
                new AllCounter<String>("all"),
                true
            );
        final Map<Integer, Double> result =
            rate.getRateByDistinguisher(string -> string.length(), Lists.newArrayList("a", "b", "c", "d"));
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(25.0, result.get(1), Double.MIN_NORMAL);
    }

    @Test
    public void getRateFullPercentageByDistinguisher() {
        final CounterRate<String> rate =
            new CounterRate<String>(
                "TestRate",
                new ObjectCounter<String>("Bs") {

                    @Override
                    public long count(final String object) {
                        return "b".equals(object) ? 1: 0;
                    }

                },
                new AllCounter<String>("all"),
                true
            );
        final Map<String, Double> result =
            rate.getRateByDistinguisher(string -> string, Lists.newArrayList("a", "b", "c"));
        Assert.assertEquals(3, result.size());
        Assert.assertEquals(0.0, result.get("a"), Double.MIN_NORMAL);
        Assert.assertEquals(100.0, result.get("b"), Double.MIN_NORMAL);
        Assert.assertEquals(0.0, result.get("c"), Double.MIN_NORMAL);
    }

    @Test
    public void getRateOne() {
        final CounterRate<String> rate =
            new CounterRate<String>("TestRate", new AllCounter<String>("all1"), new AllCounter<String>("all2"), false);
        Assert.assertEquals(1.0, rate.getRate(Lists.newArrayList("a", "b", "c")), Double.MIN_NORMAL);
    }

    @Test
    public void getRateOneByDistinguisher() {
        final CounterRate<String> rate =
            new CounterRate<String>("TestRate", new AllCounter<String>("all1"), new AllCounter<String>("all2"), false);
        final Map<String, Double> result =
            rate.getRateByDistinguisher(string -> string, Lists.newArrayList("a", "b", "c"));
        Assert.assertEquals(3, result.size());
        Assert.assertEquals(1.0, result.get("a"), Double.MIN_NORMAL);
        Assert.assertEquals(1.0, result.get("b"), Double.MIN_NORMAL);
        Assert.assertEquals(1.0, result.get("c"), Double.MIN_NORMAL);
    }

    @Test
    public void getRateOneHundredPercentage() {
        final CounterRate<String> rate =
            new CounterRate<String>("TestRate", new AllCounter<String>("all1"), new AllCounter<String>("all2"), true);
        Assert.assertEquals(100.0, rate.getRate(Lists.newArrayList("a", "b", "c")), Double.MIN_NORMAL);
    }

    @Test
    public void getRateZero() {
        final CounterRate<String> rate =
            new CounterRate<String>("TestRate", new NoneCounter<String>("none"), new AllCounter<String>("all"), false);
        Assert.assertEquals(0.0, rate.getRate(Lists.newArrayList("a", "b", "c")), Double.MIN_NORMAL);
    }

    @Test
    public void getRateZeroByDistinguisher() {
        final CounterRate<String> rate =
            new CounterRate<String>("TestRate", new NoneCounter<String>("none"), new AllCounter<String>("all"), false);
        final Map<String, Double> result =
            rate.getRateByDistinguisher(string -> string, Lists.newArrayList("a", "b", "c"));
        Assert.assertEquals(3, result.size());
        Assert.assertEquals(0.0, result.get("a"), Double.MIN_NORMAL);
        Assert.assertEquals(0.0, result.get("b"), Double.MIN_NORMAL);
        Assert.assertEquals(0.0, result.get("c"), Double.MIN_NORMAL);
    }

    @Test
    public void getRateZeroPercentage() {
        final CounterRate<String> rate =
            new CounterRate<String>("TestRate", new NoneCounter<String>("none"), new AllCounter<String>("all"), true);
        Assert.assertEquals(0.0, rate.getRate(Lists.newArrayList("a", "b", "c")), Double.MIN_NORMAL);
    }

    @Test
    public void toStringYieldsDescription() {
        final String name = "TestRate";
        final CounterRate<String> rate =
            new CounterRate<String>(name, new NoneCounter<String>("none"), new AllCounter<String>("all"), true);
        Assert.assertEquals(name, rate.toString());
    }

}
