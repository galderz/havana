package jawa.lang;

public final class Assert
{
    public static void check()
    {
        boolean enabled = false;
        assert enabled = true;
        if (!enabled)
            throw new AssertionError("assert not enabled");
    }

    private Assert()
    {
    }
}
