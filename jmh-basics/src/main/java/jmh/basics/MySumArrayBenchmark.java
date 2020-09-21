package jmh.basics;

import java.util.concurrent.ThreadLocalRandom;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@BenchmarkMode(Mode.Throughput)
public class MySumArrayBenchmark {

    @Benchmark
    public float sumFloatIterate(FloatArrays fs) {
        float sum = 0;
        float[] array = fs.next();
        for (float f : array)
            sum =+ f;

        return sum;
    }

    @Benchmark
    public float sumFloatUnrolled(FloatArrays fs) {
        float sum1 = 0, sum2 = 0, sum3 = 0, sum4 = 0;
        float[] array = fs.next();
        for (int i = 0; i < array.length; i += 4) {
            sum1 += array[i];
            sum2 += array[i + 1];
            sum3 += array[i + 2];
            sum4 += array[i + 3];
        }
        return sum1 + sum2 + sum3 + sum4;
    }

    @State(Scope.Thread)
    public static class FloatArrays {

        static final ThreadLocalRandom R = ThreadLocalRandom.current();
        static final int SPACE_SIZE = 1_000;
        final float[][] fs;

        int idx = -1;

        public FloatArrays() {
            this.fs = new float[SPACE_SIZE][];
            for (int i = 0; i < SPACE_SIZE; i++) {
                fs[i] = new float[128];
                for (int j = 0; j < 128; j++)
                    fs[i][j] = R.nextFloat();
            }
        }

        float[] next() {
            idx++;
            if (idx == SPACE_SIZE)
                idx = 0;

            return fs[idx];
        }

    }

}
