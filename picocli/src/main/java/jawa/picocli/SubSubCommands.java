package jawa.picocli;

import picocli.CommandLine;

public class SubSubCommands
{
    public static void main(String[] args)
    {
        Qollider.main("build");
        Qollider.main("clean");
        Qollider.main("test");
        Qollider.main("graal");
        Qollider.main("graal", "get");
        Qollider.main("graal", "build");

        Qollider.main("--help");
    }

    @CommandLine.Command(
        mixinStandardHelpOptions = true
    )
    static class Qollider
    {
        @CommandLine.Spec
        CommandLine.Model.CommandSpec spec;

        public static void main(String... args)
        {
            new CommandLine(new Qollider())
                .addSubcommand("build", new Build())
                .addSubcommand("clean", new Clean())
                .addSubcommand("test", new Test())
                .addSubcommand(
                    "graal"
                    , new CommandLine(new Graal())
                        .addSubcommand("build", new Graal.Build())
                        .addSubcommand("get", new Graal.Get())
                )
                .setCaseInsensitiveEnumValuesAllowed(true)
                .execute(args);
        }

        @CommandLine.Command(
            name = "build"
            , aliases = {"b"}
            , description = "Build quarkus."
            , mixinStandardHelpOptions = true
        )
        static class Build implements Runnable
        {
            @Override
            public void run()
            {
                System.out.println("Build()");
            }
        }

        @CommandLine.Command(
            name = "clean"
            , aliases = {"c"}
            , description = "Clean quarkus."
            , mixinStandardHelpOptions = true
        )
        static class Clean implements Runnable
        {
            @Override
            public void run()
            {
                System.out.println("Clean()");
            }
        }

        @CommandLine.Command(
            name = "graal"
            , aliases = {"g"}
            , description = "Graal."
            , mixinStandardHelpOptions = true
        )
        static class Graal implements Runnable
        {
            @CommandLine.Spec
            CommandLine.Model.CommandSpec spec;

            @Override
            public void run()
            {
                throw new CommandLine.ParameterException(
                    spec.commandLine()
                    , "Missing required subcommand"
                );
            }

            @CommandLine.Command(
                name = "get"
                , aliases = {"g"}
                , description = "Graal get."
                , mixinStandardHelpOptions = true
            )
            static class Get implements Runnable
            {
                @Override
                public void run()
                {
                    System.out.println("Graal.Get()");
                }
            }

            @CommandLine.Command(
                name = "Build"
                , aliases = {"b"}
                , description = "Graal build."
                , mixinStandardHelpOptions = true
            )
            static class Build implements Runnable
            {
                @Override
                public void run()
                {
                    System.out.println("Graal.Build()");
                }
            }
        }

        @CommandLine.Command(
            name = "test"
            , aliases = {"t"}
            , description = "Test quarkus."
            , mixinStandardHelpOptions = true
        )
        static class Test implements Runnable
        {
            @Override
            public void run()
            {
                System.out.println("Test()");
            }
        }
    }
}
