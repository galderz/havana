package org.sample;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.BenchmarkException;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Collection;

@State(Scope.Thread)
public class FailBenchmark
{
    @Benchmark
    public void defaultMode()
    {
        throw new ExpectedException();
    }

    public static void main(String[] args) throws RunnerException
    {
        System.out.println("FailBenchmark.main");
        Options opt = new OptionsBuilder()
            .include(FailBenchmark.class.getSimpleName())
            .forks(1)
            .jvmArgs("-ea")
            .shouldFailOnError(true)
            .build();


        try
        {
            new Runner(opt).run();
            throw new AssertionError("Expected exception to be thrown");
        }
        catch (RunnerException e)
        {
            final BenchmarkException cause = (BenchmarkException) e.getCause();
            System.out.println(cause.getSuppressed()[0] instanceof ExpectedException);
            e.printStackTrace();
        }
    }

    static final class ExpectedException extends RuntimeException
    {
    }
}
