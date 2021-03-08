package org.example.jqwik;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ArithmeticTests
{
    /**
     * IsLt(Cmp(a, b), 1) -> IsLe(a, b)
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
     * IsGe(Cmp(a, b), 1) -> IsGt(a, b)
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
     * IsLe(Cmp(a, b), -1) -> IsLt(a, b)
     */
    @Property
    void isLessEqualsCmpMinusOneConvertsToIsLessThan(@ForAll int a, @ForAll int b)
    {
        assertThat(
            Integer.compare(a, b) <= -1
            , is(a < b)
        );
    }
}
