package org.example.jmh.generators.bc2bc;

final class Asserts
{
    @SuppressWarnings({"AssertWithSideEffects", "ConstantConditions"})
    public static void needEnabledAsserts()
    {
        boolean enabled = false;
        assert enabled = true;
        if (!enabled)
            throw new AssertionError("assert not enabled");
    }
}
