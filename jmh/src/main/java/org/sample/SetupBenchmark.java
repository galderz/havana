package org.sample;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@State(Scope.Thread)
public class SetupBenchmark
{
    AtomicInteger aroundTrialCounter = new AtomicInteger();
    AtomicInteger aroundIterationCounter = new AtomicInteger();
    AtomicInteger aroundInvocationCounter = new AtomicInteger();

    @Setup(Level.Trial)
    public void beforeTrial()
    {
        System.out.println();
        System.out.println("Before trial");
        aroundTrialCounter.incrementAndGet();
    }

    @Setup(Level.Iteration)
    public void beforeIteration()
    {
        System.out.println();
        System.out.println("Before iteration");
        aroundIterationCounter.incrementAndGet();
    }

    @Setup(Level.Invocation)
    public void beforeInvocation()
    {
        aroundInvocationCounter.incrementAndGet();
    }

    @TearDown(Level.Trial)
    public void afterTrial()
    {
        System.out.println();
        System.out.println("After trial: " + aroundTrialCounter.get());
    }

    @TearDown(Level.Iteration)
    public void afterIteration()
    {
        System.out.println();
        System.out.println("After iteration: " + aroundIterationCounter.get());
    }

    @TearDown(Level.Invocation)
    public void afterInvocation()
    {
        System.out.println();
        System.out.println("After invocation: " + aroundInvocationCounter.get());
    }

    @Benchmark
    public void bench() throws InterruptedException
    {
        TimeUnit.MILLISECONDS.sleep(100);
    }
}
