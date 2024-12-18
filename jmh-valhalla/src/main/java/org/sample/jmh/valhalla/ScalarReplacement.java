package org.sample.jmh.valhalla;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class ScalarReplacement
{
    int x;
    boolean flag;

    @Setup(Level.Iteration)
    public void shake()
    {
        flag = ThreadLocalRandom.current().nextBoolean();
    }

    @Benchmark
    public int single()
    {
        MyObject o = new MyObject(x);
        return o.x;
    }

    @Benchmark
    public int split()
    {
        MyObject o;
        if (flag)
        {
            o = new MyObject(x);
        }
        else
        {
            o = new MyObject(x);
        }
        return o.x;
    }

    static class MyObject
    {
        final int x;

        public MyObject(int x)
        {
            this.x = x;
        }
    }
}
