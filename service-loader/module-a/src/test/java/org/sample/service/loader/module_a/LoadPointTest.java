package org.sample.service.loader.module_a;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class LoadPointTest
{
    @Test
    public void testLoadA()
    {
        final Point point = PointLookup.lookup();
        System.out.println(point);
        assertTrue(point.toString(), point instanceof Point);
    }
}
