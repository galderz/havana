package org.sample;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

// @State(Scope.Thread)
@State(Scope.Benchmark)
public class NoDefaultStateBenchmark
{
    double x = Math.PI;

    @Benchmark
    public void measure() {
        x++;
    }
}
