package jawa.junit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FirstUnitTest
{
    @Test
    public void whenThis_thenThat()
    {
        assertTrue(true);
    }

    @Test
    public void whenSomething_thenSomething()
    {
        assertTrue(true);
    }

    @Test
    public void whenSomethingElse_thenSomethingElse()
    {
        assertTrue(true);
    }

    @Test
    public void whenSomethingElse_thenThrowException()
    {
        throw new RuntimeException("blah");
    }
}
