package com.techyourchance.unittestingfundamentals.exercise2;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class StringDuplicatorTest {

    public StringDuplicator SUT;

    @Before
    public void setUp() {
        SUT = new StringDuplicator();
    }

    //name should be whattotest_i/p_expectedo/p
    @Test
    public void duplicate_emptyString_emptyString() {
        String result = SUT.duplicate("");
        Assert.assertThat(result, is(""));
    }

    @Test
    public void duplicate_singleChar_doubleChars() {
        String result = SUT.duplicate("a");
        Assert.assertThat(result, is("aa"));
    }

    @Test
    public void duplicate_multiChar_doubleChars() {
        String result = SUT.duplicate("aa");
        Assert.assertThat(result, is("aaaa"));
    }

    @Test
    public void duplicate_stringWithSpace_doubleOfThat() {
        String result = SUT.duplicate("aa aa ");
        Assert.assertThat(result, is("aa aa aa aa "));
    }

}