package lang.string;

import lang.Assert;

import java.util.function.Function;
import java.util.regex.Pattern;

public class Versions
{
    public static void main(String[] args)
    {
        Assert.check();

        final var V_SNAPSHOT = check(Integer.MAX_VALUE, Integer.MAX_VALUE, Version.Distribution.MANDREL).compose(Version::of);
        final var V_1_0 = check(1, 0, Version.Distribution.ORACLE).compose(Version::of);
        final var V_19_0 = check(19, 0, Version.Distribution.ORACLE).compose(Version::of);
        final var V_20_0 = check(20, 0, Version.Distribution.ORACLE).compose(Version::of);
        final var V_20_1 = check(20, 1, Version.Distribution.ORACLE).compose(Version::of);
        final var V_21_0 = check(21, 0, Version.Distribution.ORACLE).compose(Version::of);
        {
            final var version = V_SNAPSHOT.apply("GraalVM Version beb2fd6 (Mandrel Distribution) (Java Version 11.0.9-internal)");
            assert version.distro == Version.Distribution.MANDREL;
        }
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

    static Function<Version, Version> check(int major, int minor, Version.Distribution distro)
    {
        return version ->
        {
            assert version.major == major : version;
            assert version.minor == minor : version;
            assert version.distro == distro : distro;
            return version;
        };
    }

    static final class Version implements Comparable<Version>
    {
        private static final Pattern PATTERN = Pattern.compile(
            // "GraalVM Version (1.0.0|([1-9][0-9]).([0-3]).[0-9]|\\p{XDigit}{7})\\s*(\\(Mandrel Distribution\\))?\\s*"
            "GraalVM Version ((1|[1-9][0-9]).([0-2]).[0-9]|\\p{XDigit}{7})\\s*(\\(Mandrel Distribution\\))?\\s*"
        );

        static final Version UNVERSIONED = new Version(-1, -1, null);
        static final Version SNAPSHOT_ORACLE = new Version(Integer.MAX_VALUE, Integer.MAX_VALUE, Distribution.ORACLE);
        static final Version SNAPSHOT_MANDREL = new Version(Integer.MAX_VALUE, Integer.MAX_VALUE, Distribution.MANDREL);

        final int major;
        final int minor;
        final Distribution distro;

        private Version(int major, int minor, Distribution distro)
        {
            this.major = major;
            this.minor = minor;
            this.distro = distro;
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
            if (matcher.find() && matcher.groupCount() >= 3)
            {
                if (isSnapshot(matcher.group(1)))
                {
                    final var distro = matcher.group(4);
                    return isMandrel(distro) ? SNAPSHOT_MANDREL : SNAPSHOT_ORACLE;
                }
                else
                {
                    return new Version(
                        Integer.parseInt(matcher.group(2))
                        , Integer.parseInt(matcher.group(3)),
                        Distribution.ORACLE);
                }
            }

            return UNVERSIONED;
        }

        private static boolean isSnapshot(String s)
        {
            return s.length() == 7;
        }

        private static boolean isMandrel(String s)
        {
            return "(Mandrel Distribution)".equals(s);
        }

        @Override
        public String toString()
        {
            return "Version{" +
                "major=" + major +
                ", minor=" + minor +
                '}';
        }

        enum Distribution {
            ORACLE
            , MANDREL
            ;
        }
    }
}
