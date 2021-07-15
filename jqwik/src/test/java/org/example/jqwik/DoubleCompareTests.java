package org.example.jqwik;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;
import net.jqwik.api.Tuple;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DoubleCompareTests
{
    @Property
    void test000(@ForAll double a, @ForAll double b)
    {
        assertThat(
            Double.compare(a, b) < 1
            , is(a <= b)
        );
    }

    /**
     * IsLt(Cmp(a, b), 1)) => IsLe(a, b)
     */
    @Property
    void isLessThanCmpOneConvertsToIsLessEquals(@ForAll("allDoubles") double a, @ForAll("allDoubles") double b)
    {
        assertThat(
            Double.compare(a, b) < 1
            , is(a <= b)
        );
    }

    @Provide
    Arbitrary<Double> allDoubles()
    {
        return Arbitraries.frequencyOf(
            Tuple.of(20, Arbitraries.doubles()),
            Tuple.of(1, Arbitraries.of(Double.MIN_VALUE, -Double.MIN_VALUE, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY))
        );
    }
}
