package reflection;

public final class Assert
{
    public static void check()
    {
        boolean enabled = false;
        //noinspection AssertWithSideEffects
        assert enabled = true;
        //noinspection ConstantConditions
        if (!enabled)
            throw new AssertionError("assert not enabled");
    }

    private Assert()
    {
    }
}
