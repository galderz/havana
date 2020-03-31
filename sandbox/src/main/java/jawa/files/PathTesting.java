package jawa.files;

import java.nio.file.Path;

public class PathTesting
{
    public static void main(String[] args)
    {
        EasyToTest.main();
        EasyToTest.test();
    }

    private static class EasyToTest
    {
        public static void main()
        {
            final var printer = new Printer() {
                @Override
                public void print(String msg)
                {
                    System.out.println(msg);
                }
            };
            skipOrExecute(Marker.of("/Users/g"), printer); // skip
            skipOrExecute(Marker.of("/x/y/z"), printer); // execute
        }

        public static void test()
        {
            final var printer = new Printer();
            skipOrExecute(new Marker("/Users/g", true), printer); // skip
            System.out.println(printer.last);
            skipOrExecute(new Marker("/x/y/z", false), printer); // execute
            System.out.println(printer.last);
        }

        private static void skipOrExecute(Marker marker, Printer printer)
        {
            if (marker.exists)
            {
                printer.print("skip");
                return;
            }

            printer.print("execute");
        }

        final static class Marker
        {
            final String name;
            final boolean exists;

            private Marker(String name, boolean exists)
            {
                this.name = name;
                this.exists = exists;
            }

            static Marker of(String name)
            {
                return new Marker(name, Path.of(name).toFile().exists());
            }
        }

        static class Printer
        {
            String last;

            public void print(String msg)
            {
                last = msg;
            }
        }
    }

    @SuppressWarnings("unused")
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
