package org.sample;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class StatesBenchmark
{
    @State(Scope.Benchmark)
    public static class BenchmarkState {
        volatile double x = Math.PI;
    }

    @State(Scope.Thread)
    public static class ThreadState {
        volatile double x = Math.PI;
    }

    @Benchmark
    public void measureUnshared(ThreadState ts1, ThreadState ts2) {
        ts1.x++;
        ts2.x++;
        System.out.println(ts1 + " " + ts2);
    }

    @Benchmark
    public void measureShared(BenchmarkState bs1, BenchmarkState bs2) {
        bs1.x++;
        bs2.x++;
        System.out.println(bs1 + " " + bs2);
    }
}
