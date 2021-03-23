package org.example.jqwik;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class IntegetCompareTests
{
    /**
     * IsLt(Cmp(a, b), 1)) => IsLe(a, b)
     */
    @Property
    void isLessThanCmpOneConvertsToIsLessEquals(@ForAll int a, @ForAll int b)
    {
        assertThat(
            Integer.compare(a, b) < 1
            , is(a <= b)
        );
    }

    /**
     * IsGe(Cmp(a, b), 1)) => IsGt(a, b)
     */
    @Property
    void isGreaterEqualsCmpOneConvertsToIsGreaterThan(@ForAll int a, @ForAll int b)
    {
        assertThat(
            Integer.compare(a, b) >= 1
            , is(a > b)
        );
    }

    /**
     * IsGt(1, Cmp(a, b)) => IsLe(a, b)
     */
    @Property
    void iGreaterThanOneCmpConvertsToIsLessEquals(@ForAll int a, @ForAll int b)
    {
        assertThat(
            1 > Integer.compare(a, b)
            , is(a <= b)
        );
    }

    /**
     * IsLe(1, Cmp(a, b)) => IsGt(a, b)
     */
    @Property
    void isLessEqualsOneCmpConvertsToIsGreaterThan(@ForAll int a, @ForAll int b)
    {
        assertThat(
            1 <= Integer.compare(a, b)
            , is(a > b)
        );
    }

    /**
     * IsGt(Cmp(a, b), -1)) => IsGe(a, b)
     */
    @Property
    void isGreaterThanCmpMinusOneConvertsToGreaterEquals(@ForAll int a, @ForAll int b)
    {
        assertThat(
            Integer.compare(a, b) > -1
            , is(a >= b)
        );
    }

    /**
     * IsLe(Cmp(a, b), -1)) => IsLt(a, b)
     */
    @Property
    void isLessEqualsCmpMinusOneConvertsToLessThan(@ForAll int a, @ForAll int b)
    {
        assertThat(
            Integer.compare(a, b) <= -1
            , is(a < b)
        );
    }

    /**
     * IsLt(-1, Cmp(a, b)) => IsGe(a, b)
     */
    @Property
    void isLessThanMinusOneCmpConvertsToGreaterEquals(@ForAll int a, @ForAll int b)
    {
        assertThat(
            -1 < Integer.compare(a, b)
            , is(a >= b)
        );
    }

    /**
     * IsGe(-1, Cmp(a, b)) => IsLt(a, b)
     */
    @Property
    void isGreaterEqualsMinusOneCmpConvertsToLessThan(@ForAll int a, @ForAll int b)
    {
        assertThat(
            -1 >= Integer.compare(a, b)
            , is(a < b)
        );
    }

    /**
     * Neg(IsLt(Cmp(a, b), c)) => IsGe(Cmp(a, b), c))
     */
    @Property
    void notLessThanConvertsToGreaterEquals(@ForAll int a, @ForAll int b, @ForAll int c)
    {
        assertThat(
            !(Integer.compare(a, b) < c)
            , is(Integer.compare(a, b) >= c)
        );
    }
}
