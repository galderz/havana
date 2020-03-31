package jawa.files;

import java.nio.file.Path;

public class PathTesting
{
    public static void main(String[] args)
    {
        HardToTest.main();
    }

    private static class HardToTest
    {
        public static void main()
        {
            skipOrExecute("/Users/g"); // skip
            skipOrExecute("/x/y/z"); // execute
        }

        private static void skipOrExecute(String first)
        {
            final var path = Path.of(first);
            if (OperatingSystem.fileExists(path))
            {
                OperatingSystem.println("skip");
                return;
            }

            OperatingSystem.println("execute");
        }

        static class OperatingSystem
        {
            // Function<Path, Boolean>
            static boolean fileExists(Path path)
            {
                return path.toFile().exists();
            }

            // Consumer<String>
            static void println(String msg)
            {
                System.out.println(msg);
            }
        }
    }

}
