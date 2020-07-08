package p.picocli;

import picocli.CommandLine;

import java.io.File;
import java.util.Arrays;

public class TarCli
{
    public static void main(String[] args)
    {
        String[] theseArgs = {"-c", "--file", "result.tar", "file1.txt", "file2.txt"};
        Tar tar = new Tar();
        new CommandLine(tar).parseArgs(theseArgs);

        assert !tar.helpRequested;
        System.out.println(!tar.helpRequested);

        assert tar.create;
        System.out.println(!tar.create);

        assert tar.archive.equals(new File("result.tar"));
        System.out.println(tar.archive);

        assert Arrays.equals(tar.files, new File[]{new File("file1.txt"), new File("file2.txt")});
        System.out.println(Arrays.toString(tar.files));
    }

    static class Tar
    {
        @CommandLine.Option(names = "-c", description = "create a new archive")
        boolean create;

        @CommandLine.Option(names = {"-f", "--file"}, paramLabel = "ARCHIVE", description = "the archive file")
        File archive;

        @CommandLine.Parameters(paramLabel = "FILE", description = "one ore more files to archive")
        File[] files;

        @CommandLine.Option(names = {"-h", "--help"}, usageHelp = true, description = "display a help message")
        private boolean helpRequested = false;
    }
}
