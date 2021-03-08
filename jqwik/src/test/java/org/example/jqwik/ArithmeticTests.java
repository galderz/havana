package org.example.jqwik;

import net.jqwik.api.Disabled;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

public class ArithmeticTests
{
    @Property
    void isLessThanCmpOneConvertsToLessEquals(@ForAll int a, @ForAll int b)
    {
        // IsLt(Cmp(a, b), 1) -> IsLe(a, b)
        assertThat(
            Integer.compare(a, b) < 1
            , is(a <= b)
        );
    }
}
