package lang.string;

import lang.Assert;

public class Versions
{
    public static void main(String[] args)
    {
        Assert.check();

        {
            final var version = new Version(20, 0);
            final var other = new Version(19, 0);
            assert version.compareTo(other) > 0;
        }
        {
            final var version = new Version(20, 0);
            final var other = new Version(21, 0);
            assert version.compareTo(other) < 0;
        }
        {
            final var version = new Version(20, 0);
            final var other = new Version(20, 0);
            assert version.compareTo(other) == 0;
        }
        {
            final var version = new Version(20, 1);
            final var other = new Version(20, 0);
            assert version.compareTo(other) > 0;
        }
        {
            final var version = new Version(20, 0);
            final var other = new Version(20, 1);
            assert version.compareTo(other) < 1;
        }
    }

    static final class Version implements Comparable<Version>
    {
        final int major;
        final int minor;

        Version(int major, int minor)
        {
            this.major = major;
            this.minor = minor;
        }

        @Override
        public int compareTo(Version o)
        {
            if (major > o.major)
                return 1;

            if (major == o.major)
            {
                if (minor > o.minor)
                    return 1;
                else if (minor == o.minor)
                    return 0;
            }

            return -1;
        }
    }
}
