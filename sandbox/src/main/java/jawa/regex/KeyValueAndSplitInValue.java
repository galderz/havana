package jawa.regex;

import java.util.Arrays;

public class KeyValueAndSplitInValue
{
    public static void main(String[] args)
    {
        {
            String s = "quarkus=-rf,:quarkus-elytron-security-ldap-integration-test";
            final var splits = Arrays.asList(s.split("="));
            System.out.printf("%d:%s%n", splits.size(), splits);
        }

        {
            String s = "quarkus-platform=-Dcamel-quarkus.version=1.1.0-SNAPSHOT,-Dquarkus.version=999-SNAPSHOT";
            final var splits = Arrays.asList(s.split("="));
            System.out.printf("%d:%s%n", splits.size(), splits);
        }

        {
            String s = "quarkus-platform=-Dcamel-quarkus.version=1.1.0-SNAPSHOT,-Dquarkus.version=999-SNAPSHOT";
            final var splits = Arrays.asList(s.split("=", 2));
            System.out.printf("%d:%s%n", splits.size(), splits);
        }
    }
}
