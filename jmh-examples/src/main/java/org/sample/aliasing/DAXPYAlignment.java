package org.sample.aliasing;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
public class DAXPYAlignment
{
    @Param({"250", "256", "1000", "1024"})
    int size;

    double s;
    double[] a;
    double[] b;

    @Setup(Level.Trial)
    public void init()
    {
        s = ThreadLocalRandom.current().nextDouble();
        a = createDoubleArray(size);
        b = createDoubleArray(size);
    }

    private double[] createDoubleArray(int size)
    {
        double[] result = new double[size];
        for (int i = 0; i < size; i++)
        {
            result[i] = ThreadLocalRandom.current().nextDouble();
        }
        return result;
    }

    @Benchmark
    public double[] daxpy()
    {
        for (int i = 0; i < a.length; ++i)
        {
            a[i] += s * b[i];
        }
        return a;
    }
}
