//usr/bin/env jbang "$0" "$@" ; exit $?
//JAVAC_OPTIONS --enable-preview -Xlint:preview -source 15
//JAVA_OPTIONS --enable-preview
//DEPS org.apache.logging.log4j:log4j-core:2.13.0

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class build_graal
{
    public static void main(String[] args)
    {
        final var graalHome = Path.of("/Users/g/1/graal-graal/graal");

        OperatingSystem
            .exec(graalHome.resolve("substratevm"))
            .apply("""
                mx -V build
                mx -V maven-install
                """);

        OperatingSystem
            .exec(graalHome.resolve("substratevm"))
            .apply("""
                mx -V build
                mx -V maven-install
                """);

        OperatingSystem
            .exec(graalHome.resolve("truffle"))
            .apply("""
                mx -V maven-install
                """);

        OperatingSystem
            .exec(graalHome.resolve("sdk"))
            .apply("""
                mx -V maven-install
                """);

        OperatingSystem
            .exec(graalHome.resolve("compiler"))
            .apply("""
                mx -V maven-install
                """);
    }

    static class OperatingSystem
    {
        static final Logger logger = LogManager.getLogger(OperatingSystem.class);

        static Function<String, Void> exec(Path path)
        {
            return script ->
            {
                script.lines()
                    .map(line ->
                        new Command(
                            Arrays.stream(line.split("\\s+"))
                            , path
                            , Stream.empty()
                        ))
                    .forEach(command -> exec().apply(command));

                return null;
            };
        }

        static Function<Command, Void> exec()
        {
            return OperatingSystem::exec;
        }

        private static Void exec(Command command)
        {
            final var commandList = command.command.collect(Collectors.toList());
            logger.debug("Execute {} in {}", commandList, command.directory);
            try
            {
                var processBuilder = new ProcessBuilder(commandList)
                    .directory(command.directory.toFile())
                    .inheritIO();

                command.envVars.forEach(
                    envVar -> processBuilder.environment()
                        .put(envVar.name, envVar.value)
                );

                Process process = processBuilder.start();

                if (process.waitFor() != 0)
                {
                    throw new RuntimeException(
                        "Failed, exit code: " + process.exitValue()
                    );
                }

                return null;
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }

        static class Command
        {
            final Stream<String> command;
            final Path directory;
            final Stream<EnvVar> envVars;

            Command(Stream<String> command, Path directory, Stream<EnvVar> envVars)
            {
                this.command = command;
                this.directory = directory;
                this.envVars = envVars;
            }
        }

        static class EnvVar
        {
            final String name;
            final String value;

            EnvVar(String name, String value)
            {
                this.name = name;
                this.value = value;
            }
        }
    }
}

