package jawa.picocli;

import picocli.CommandLine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestCli
{
    public static void main(String[] ignore)
    {
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

        @CommandLine.Option(names = "--additional-test-args", split = "\\|")
        private final Map<String, String> additionalTestArgs = new HashMap<>();

        @CommandLine.Option(names = "--also-test", split = ",")
        private final List<String> alsoTest = new ArrayList<>();
    }
}
