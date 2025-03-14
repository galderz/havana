package files;

import java.nio.file.Path;

public class Relativize
{
    public static void main(String[] args)
    {
        Path file = Path.of("./fibula-it/target/test-classes/org/mendrugo/fibula/it/profilers/jmh_generated/ProfilerIB_bench_jmhTest.class");
        Path base = Path.of("./fibula-it/target/test-classes");
        System.out.println(file.relativize(base));
        System.out.println(base.relativize(file));
    }
}
