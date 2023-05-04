///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli:4.6.3

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.concurrent.Callable;

@Command(name = "buffer", mixinStandardHelpOptions = true, version = "buffer 0.1",
    description = "buffer made with jbang")
class buffer implements Callable<Integer>
{
    @Parameters(index = "0", description = "The greeting to print", defaultValue = "World!")
    private String greeting;

    public static void main(String... args)
    {
        int exitCode = new CommandLine(new buffer()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception
    {
        String plist = """
            <?xml version="1.0" encoding="UTF-8"?>
            <!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
            <plist version="1.0">
            	<dict>
            		<key>Label</key>
            		<string>havana.buffer</string>
            		<key>EnvironmentVariables</key>
            		<dict>
            			<key>JAVA_HOME</key>
            			<string>/opt/java-17</string>
            		</dict>
            		<key>ProgramArguments</key>
            		<array>
            			<string>/opt/jbang/bin/jbang</string>
            			<string>/Users/g/Downloads/hellocli.java</string>
            		</array>
            		<key>StandardInPath</key>
            		<string>/tmp/buffer.stdin</string>
            		<key>StandardOutPath</key>
            		<string>/tmp/buffer.stdout</string>
            		<key>StandardErrorPath</key>
            		<string>/tmp/buffer.stderr</string>
            		<key>RunAtLoad</key>
              		<true/>
            	</dict>
            </plist>
            """;

        final String userHome = System.getProperty("user.home");
        final Path agentsPath = Path.of("Library", "LaunchAgents", "havana.buffer.plist");
        Files.writeString(Path.of(userHome).resolve(agentsPath), plist);
        return 0;
    }
}
