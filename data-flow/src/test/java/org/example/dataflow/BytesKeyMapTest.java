package org.example.dataflow;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class BytesKeyMapTest
{
    @Test
    void test000()
    {
        var map = new BytesKeyMap();
        var key = new byte[]{1, 2, 3};

        BytesKeyMap.put(key, "hello", map);

        assertThat(map.keys[0].key(), is(new byte[]{1, 2, 3}));
        assertThat(map.values[0], is("hello"));
    }
}
