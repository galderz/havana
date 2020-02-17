//usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS org.apache.logging.log4j:log4j-core:2.13.0

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class update_jdk
{
    public static void main(String[] args) throws Exception
    {
        final var hgPull = new OperatingSystem.Command(
            Stream.of("hg", "pull", "--update")
            , new File("/Users/g/1/jdkjdk")
            , Stream.empty()
        );

        final var configureBash = new OperatingSystem.Command(
            Stream.of(
                "bash"
                , "configure"
                , "--with-debug-level=fastdebug"
                , "--with-target-bits=64"
                , "--with-boot-jdk=/opt/java-13"
                , "--with-conf-name=graal-fastdebug"
                , "--disable-warnings-as-errors"
                , "CC=/usr/bin/gcc"
                , "CXX=/usr/bin/g++"
            )
            , new File("/Users/g/1/jdkjdk")
            , Stream.empty()
        );

        final var CONF = new OperatingSystem.EnvVar(
            "CONF"
            , "graal-fastdebug"
        );

        final var buildJdk = new OperatingSystem.Command(
            Stream.of(
                "make"
                , "clean"
                , "graal-builder-image"
            )
            , new File("/Users/g/1/jdkjdk")
            , Stream.of(
                CONF
            )
        );

        OperatingSystem.exec().apply(hgPull);
        OperatingSystem.exec().apply(configureBash);
        OperatingSystem.exec().apply(buildJdk);
    }
}

class OperatingSystem
{
    static final Logger logger = LogManager.getLogger(OperatingSystem.class);

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
                .directory(command.directory)
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
        final File directory;
        final Stream<EnvVar> envVars;

        Command(Stream<String> command, File directory, Stream<EnvVar> envVars)
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
