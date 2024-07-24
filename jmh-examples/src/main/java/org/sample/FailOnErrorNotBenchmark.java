package org.sample;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

@State(Scope.Thread)
public class FailOnErrorNotBenchmark
{
    @Benchmark
    public void avoidFailure()
    {
        throw new IllegalArgumentException("Provoke exception in @Benchmark but do not cause failure");
    }

    public static void main(String[] args) throws Exception
    {
        final Options opt = new OptionsBuilder()
            .include(FailOnErrorNotBenchmark.class.getCanonicalName() + ".avoidFailure")
            .forks(1)
            .measurementIterations(1)
            .measurementTime(TimeValue.milliseconds(100))
            .warmupIterations(0)
            .build();

        new Runner(opt).run();
    }
}
