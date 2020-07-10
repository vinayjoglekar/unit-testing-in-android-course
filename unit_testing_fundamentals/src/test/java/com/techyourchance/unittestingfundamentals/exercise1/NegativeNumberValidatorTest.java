package com.techyourchance.unittestingfundamentals.exercise1;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class NegativeNumberValidatorTest {

    NegativeNumberValidator SUT;

    @Before
    public void setUp() {
        SUT = new NegativeNumberValidator();
    }

    @Test
    public void test_negative_number() {
        boolean result = SUT.isNegative(-100);
        Assert.assertThat(result, is(true));
    }

    @Test
    public void test_zero_case() {
        boolean result = SUT.isNegative(0);
        Assert.assertThat(result, is(false));
    }

    @Test
    public void test_positive_case() {
        boolean result = SUT.isNegative(1);
        Assert.assertThat(result, is(false));
    }

}