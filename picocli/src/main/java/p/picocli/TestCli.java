package p.picocli;

import picocli.CommandLine;

import java.util.ArrayList;
import java.util.List;

public class TestCli
{
    public static void main(String[] ignore)
    {
        Assert.check();

        {
            System.out.println("> Test multiple with added test parameters");
            String[] args = {
                "--additional-test-args"
                , "quarkus=-rf,:quarkus-elytron-security-ldap-integration-test"
                , "--additional-test-args"
                , "quarkus-platform=-Dcamel-quarkus.version=1.1.0-SNAPSHOT,-Dquarkus.version=999-SNAPSHOT"
                , "--also-test"
                , "https://github.com/quarkusio/quarkus-platform/tree/master"
            };
            final var test = new Test();
            new CommandLine(test).parseArgs(args);
            assert test.additionalTestArgs.size() == 2;
            assert test.additionalTestArgs.get(0).equals("quarkus=-rf,:quarkus-elytron-security-ldap-integration-test");
            assert test.additionalTestArgs.get(1).equals("quarkus-platform=-Dcamel-quarkus.version=1.1.0-SNAPSHOT,-Dquarkus.version=999-SNAPSHOT");
            assert test.alsoTest.size() == 1;
            assert test.alsoTest.get(0).equals("https://github.com/quarkusio/quarkus-platform/tree/master");
        }

        {
            System.out.println("> Test only quarkus-platform with specific test parameters");
            String[] args = {
                "--suites"
                , "quarkus-platform"
                , "--additional-test-args"
                , "quarkus-platform=-rf,:quarkus-universe-integration-tests-camel-aws"
                , "--also-test"
                , "https://github.com/quarkusio/quarkus-platform/tree/master"
            };
            final var test = new Test();
            new CommandLine(test).parseArgs(args);
            System.out.println(test.suites);
            System.out.println(test.additionalTestArgs);
            System.out.println(test.alsoTest);
        }

        {
            System.out.println("> Test only quarkus-platform");
            String[] args = {
                "--suites"
                , "quarkus-platform"
                , "--also-test"
                , "https://github.com/quarkusio/quarkus-platform/tree/master"
            };
            final var test = new Test();
            new CommandLine(test).parseArgs(args);
            System.out.println(test.suites);
            System.out.println(test.additionalTestArgs);
            System.out.println(test.alsoTest);
        }

        {
            System.out.println("> Test quarkus with specific test parameters");
            String[] args = {
                "--additional-test-args"
                , "quarkus=-rf,:quarkus-integration-test-tika"
            };
            final var test = new Test();
            new CommandLine(test).parseArgs(args);
            System.out.println(test.suites);
            System.out.println(test.additionalTestArgs);
            System.out.println(test.alsoTest);
        }

        {
            System.out.println("> Test quarkus and quarkus platform");
            String[] args = {
                "--also-test"
                , "https://github.com/quarkusio/quarkus-platform"
            };
            final var test = new Test();
            new CommandLine(test).parseArgs(args);
            System.out.println(test.suites);
            System.out.println(test.additionalTestArgs);
            System.out.println(test.alsoTest);
        }

        {
            String[] args = {};
            final var test = new Test();
            new CommandLine(test).parseArgs(args);
            System.out.println(test.suites);
            System.out.println(test.alsoTest);
        }
    }

    static class Test
    {
        @CommandLine.Option(names = "--suites", split = ",")
        private final List<String> suites = new ArrayList<>();

        @CommandLine.Option(names = "--additional-test-args")
        private final List<String> additionalTestArgs = new ArrayList<>();

        @CommandLine.Option(names = "--also-test", split = ",")
        private final List<String> alsoTest = new ArrayList<>();
    }
}
