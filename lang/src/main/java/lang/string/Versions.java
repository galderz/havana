package lang.string;

import lang.Assert;

import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Pattern;

public class Versions
{
    public static void main(String[] args)
    {
        Assert.check();

        final var V_1_0 = check(1, 0).compose(Version::of);
        final var V_19_0 = check(19, 0).compose(Version::of);
        final var V_20_0 = check(20, 0).compose(Version::of);
        final var V_20_1 = check(20, 1).compose(Version::of);
        final var V_21_0 = check(21, 0).compose(Version::of);
        {
            final var version = V_20_0.apply("GraalVM Version 20.0.0");
            final var other = V_19_0.apply("GraalVM Version 19.0.0");
            assert version.compareTo(other) > 0;
        }
        {
            final var version = V_20_0.apply("GraalVM Version 20.0.0");
            final var other = V_21_0.apply("GraalVM Version 21.0.0");
            assert version.compareTo(other) < 0;
        }
        {
            final var version = V_20_0.apply("GraalVM Version 20.0.1");
            final var other = V_20_0.apply("GraalVM Version 20.0.1");
            assert version.compareTo(other) == 0;
        }
        {
            final var version = V_20_1.apply("GraalVM Version 20.1.0");
            final var other = V_20_0.apply("GraalVM Version 20.0.0");
            assert version.compareTo(other) > 0;
        }
        {
            final var version = V_20_0.apply("GraalVM Version 20.0.0");
            final var other = V_20_1.apply("GraalVM Version 20.1.0");
            assert version.compareTo(other) < 1;
        }
        {
            final var version = V_1_0.apply("GraalVM Version 1.0.0");
            final var other = V_20_1.apply("GraalVM Version 20.1.0");
            assert version.compareTo(other) < 1;
        }
    }

    static Function<Version, Version> check(int major, int minor)
    {
        return version ->
        {
            assert version.major == major : version;
            assert version.minor == minor : version;
            return version;
        };
    }

    static final class Version implements Comparable<Version>
    {
        private static final Pattern PATTERN = Pattern.compile(
            "GraalVM Version (1.0.0|([1-9][0-9]).([0-3]).[0-9])\\s*"
        );

        final int major;
        final int minor;

        private Version(int major, int minor)
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

        static Version of(String version)
        {
            final var matcher = PATTERN.matcher(version);
            if (matcher.find() && matcher.groupCount() == 3)
            {
                if (Objects.isNull(matcher.group(2)))
                {
                    return new Version(1, 0);
                }
                else
                {
                    return new Version(
                        Integer.parseInt(matcher.group(2))
                        , Integer.parseInt(matcher.group(3))
                    );
                }
            }

            return new Version(-1, -1);
        }

        @Override
        public String toString()
        {
            return "Version{" +
                "major=" + major +
                ", minor=" + minor +
                '}';
        }
    }
}
