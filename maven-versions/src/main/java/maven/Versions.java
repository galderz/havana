package maven;

import org.apache.maven.artifact.versioning.ComparableVersion;

public class Versions
{
    public static void main(String[] args)
    {
        needEnabledAsserts();

        assert
            new ComparableVersion("20.1.0.1-0.redhat-00005").compareTo(
            new ComparableVersion("20.1.0.1-0.redhat-00006"))
            < 0;

        assert
            new ComparableVersion("20.1.0.1-0.redhat-00005").compareTo(
            new ComparableVersion("20.1.0.1-1.redhat-00005"))
            < 0;

        assert
            new ComparableVersion("20.1.0.1-0.redhat-00005").compareTo(
            new ComparableVersion("20.1.0.2-0.redhat-00005"))
            < 0;

        assert
            new ComparableVersion("20.1.0.1-0.redhat-00005").compareTo(
            new ComparableVersion("20.1.1.1-0.redhat-00005"))
            < 0;

        assert
            new ComparableVersion("20.1.0.2-0.redhat-00005").compareTo(
            new ComparableVersion("20.1.0.2-0.redhat-00005"))
            == 0;
    }

    @SuppressWarnings({"AssertWithSideEffects", "ConstantConditions"})
    static void needEnabledAsserts()
    {
        boolean enabled = false;
        assert enabled = true;
        if (!enabled)
            throw new AssertionError("assert not enabled");
    }
}
