package junit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NumbersTest
{
    @Test
    public void testCompareIntLong()
    {
        assertEquals(Integer.valueOf(100000), Long.valueOf(100000L));
    }
}
