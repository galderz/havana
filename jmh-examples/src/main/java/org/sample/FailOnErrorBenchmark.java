package org.sample;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.BenchmarkException;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@State(Scope.Thread)
public class FailOnErrorBenchmark
{
    @Benchmark
    public void customException()
    {
        throw new CustomException();
    }

    @Benchmark
    public void singleException()
    {
        throw new ExpectedException();
    }

    @Benchmark
    public void chainedExceptions()
    {
        try
        {
            generateException();
        }
        catch (NullPointerException e)
        {
            throw new RuntimeException("Something went wrong", e);
        }
    }

    private static void generateException()
    {
        throw new NullPointerException();
    }

    public static void main(String[] args) throws RunnerException
    {
        System.out.println("FailBenchmark.main");
        Options opt = new OptionsBuilder()
            .include(FailOnErrorBenchmark.class.getSimpleName())
            .forks(0)
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
