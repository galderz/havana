//usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli:4.2.0

import picocli.CommandLine;

import java.util.Map;
import java.util.concurrent.Callable;

public class picocli_fix_params implements Callable<Integer>
{
    @CommandLine.Option(names = "-fix", split = "\\|")
    Map<Integer, String> message;

    public static void main(String[] args)
    {
        int exitCode = new CommandLine(new picocli_fix_params()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception
    {
        System.out.println(message);
        return 0;
    }
}
