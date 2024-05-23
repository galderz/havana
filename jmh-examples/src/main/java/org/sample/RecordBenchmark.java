package org.sample;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class RecordBenchmark
{
    public record Pos(double x, double y, double z, float yaw, float pitch) {}

    @State(Scope.Benchmark)
    public static class BenchmarkState
    {
        volatile Pos isEqualsPos1 = new Pos(0.0, 1.0, 2.0, 3.0f, 4.0f);
        volatile Pos isEqualsPos2 = new Pos(0.0, 1.0, 2.0, 3.0f, 4.0f);
    }

    @Benchmark
    public boolean equalsPositions(BenchmarkState state)
    {
        return state.isEqualsPos1.equals(state.isEqualsPos2);
    }
}
