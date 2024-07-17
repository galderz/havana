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
public class SetupTearDownThread
{
    final AtomicInteger trialCounter = new AtomicInteger();
    final AtomicInteger beforeIterationCounter = new AtomicInteger();
    final AtomicInteger afterIterationCounter = new AtomicInteger();
    final AtomicInteger beforeInvocationCounter = new AtomicInteger();
    final AtomicInteger afterInvocationCounter = new AtomicInteger();

    @Setup(Level.Trial)
    public void beforeTrial()
    {
        trialCounter.incrementAndGet();
    }

    @Setup(Level.Iteration)
    public void beforeIteration()
    {
        beforeIterationCounter.incrementAndGet();
    }

    @Setup(Level.Invocation)
    public void beforeInvocation()
    {
        beforeInvocationCounter.incrementAndGet();
    }

    @TearDown(Level.Trial)
    public void afterTrial()
    {
        System.out.println();
        System.out.println("After trial, trial counter is: " + trialCounter.get() + ", should be 1");
        System.out.println("After trial, before iteration counter is: " + beforeIterationCounter.get() + ", should be 2");
        System.out.println("After trial, after iteration counter is: " + afterIterationCounter.get() + ", should be 2");

        System.out.println("After trial, before invocation counter is: " + beforeInvocationCounter.get() + ", should be bigger than 2");
        System.out.println("After trial, after invocation counter is: " + afterInvocationCounter.get() + ", should be bigger than 2");
    }

    @TearDown(Level.Iteration)
    public void afterIteration()
    {
        afterIterationCounter.incrementAndGet();
    }

    @TearDown(Level.Invocation)
    public void afterInvocation()
    {
        afterInvocationCounter.incrementAndGet();
    }

    @Benchmark
    public void bench() throws InterruptedException
    {
        TimeUnit.MILLISECONDS.sleep(100);
    }
}
