package com.techyourchance.unittestingfundamentals.exercise3;

import com.techyourchance.unittestingfundamentals.example3.Interval;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class IntervalsAdjacencyDetectorTest {

    IntervalsAdjacencyDetector SUT;

    @Before
    public void setUp() {
        SUT = new IntervalsAdjacencyDetector();
    }

    //adjacency_interval1endIsAdjacentToInterval2Start_trueAdjacency
    @Test
    public void adjacency_interval1endIsAdjacentToInterval2Start_trueAdjacency() {
        Interval interval1 = new Interval(10, 20);
        Interval interval2 = new Interval(20, 25);
        boolean result = SUT.isAdjacent(interval1, interval2);
        Assert.assertThat(result, is(true));
    }

    //10 20 5 10 adjacency_interval1StartIsAdjacentToInterval2End_trueAdjacency
    @Test
    public void adjacency_interval1StartIsAdjacentToInterval2End_trueAdjacency() {
        Interval interval1 = new Interval(10, 20);
        Interval interval2 = new Interval(5, 10);
        boolean result = SUT.isAdjacent(interval1, interval2);
        Assert.assertThat(result, is(true));
    }

    //adjacency_interval1EndIsAdjacentToInterval2SEnd_falseAdjacency
    @Test
    public void adjacency_interval1EndIsAdjacentToInterval2SEnd_falseAdjacency() {
        Interval interval1 = new Interval(10, 20);
        Interval interval2 = new Interval(15, 20);
        boolean result = SUT.isAdjacent(interval1, interval2);
        Assert.assertThat(result, is(false));
    }

    //adjacency_interval1StartIsAdjacentToInterval2Start_falseAdjacency
    @Test
    public void adjacency_interval1StartIsAdjacentToInterval2Start_falseAdjacency() {
        Interval interval1 = new Interval(15, 17);
        Interval interval2 = new Interval(15, 20);
        boolean result = SUT.isAdjacent(interval1, interval2);
        Assert.assertThat(result, is(false));
    }

    @Test
    public void adjacency_interval1EndBeforeInterval2Starts_falseAdjacency() {
        Interval interval1 = new Interval(8, 13);
        Interval interval2 = new Interval(15, 20);
        boolean result = SUT.isAdjacent(interval1, interval2);
        Assert.assertThat(result, is(false));
    }

    @Test
    public void adjacency_interval1StartsBeforeInterval2Ends_falseAdjacency() {
        Interval interval1 = new Interval(8, 13);
        Interval interval2 = new Interval(5, 7);
        boolean result = SUT.isAdjacent(interval1, interval2);
        Assert.assertThat(result, is(false));
    }

    @Test
    public void adjacency_sameIntervals_falseAdjacency() {
        Interval interval1 = new Interval(8, 13);
        Interval interval2 = new Interval(8, 13);
        boolean result = SUT.isAdjacent(interval1, interval2);
        Assert.assertThat(result, is(false));
    }
}