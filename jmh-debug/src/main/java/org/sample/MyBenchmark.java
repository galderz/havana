/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.sample;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MyBenchmark
{

    public static final AtomicInteger TICKER = new AtomicInteger();

    @State(Scope.Benchmark)
    public static class MyState
    {
        private volatile int tickSetInstance;
        private volatile int tickSetIteration;
        private volatile int tickSetInvocation;
        private volatile int tickTearInstance;
        private volatile int tickTearIteration;
        private volatile int tickTearInvocation;
        private volatile int tickRun;

        @Setup(Level.Trial)
        public void setupInstance()
        {
            tickSetInstance = TICKER.incrementAndGet();
        }

        @Setup(Level.Iteration)
        public void setupIteration()
        {
            tickSetIteration = TICKER.incrementAndGet();
        }

        @Setup(Level.Invocation)
        public void setupInvocation()
        {
            tickSetInvocation = TICKER.incrementAndGet();
        }

        @TearDown(Level.Invocation)
        public void tearDownInvocation()
        {
            tickTearInvocation = TICKER.incrementAndGet();
        }

        @TearDown(Level.Iteration)
        public void tearDownIteration()
        {
            tickTearIteration = TICKER.incrementAndGet();
        }

        @TearDown(Level.Trial)
        public void tearDownInstance()
        {
            tickTearInstance = TICKER.incrementAndGet();

            System.out.println("Setup/instance called before setup/iteration: " + (tickSetInstance < tickSetIteration));
            System.out.println("Setup/iteration called before setup/invocation: " + (tickSetIteration < tickSetInvocation));
            System.out.println("Setup/invocation called before run: " + (tickSetInvocation < tickRun));
            System.out.println("Run called before tear/invocation: " + (tickRun < tickTearInvocation));
            System.out.println("Tear/invocation called before tear/iteration: " + (tickTearInvocation < tickTearIteration));
            System.out.println("Tear/iteration called before tear/instance: " + (tickTearIteration < tickTearInstance));
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.All)
    @Warmup(iterations = 0)
    @Measurement(iterations = 1, time = 100, timeUnit = TimeUnit.MILLISECONDS)
    @Fork(1)
    @Threads(1)
    public void test(MyState state)
    {
        state.tickRun = TICKER.incrementAndGet();
        try
        {
            TimeUnit.MILLISECONDS.sleep(10);
        }
        catch (InterruptedException e)
        {
            throw new IllegalStateException(e);
        }
    }

//    @Test
//    public void invokeAPI() throws RunnerException
//    {
//        for (int c = 0; c < Fixtures.repetitionCount(); c++) {
//            Options opt = new OptionsBuilder()
//                .include(Fixtures.getTestMask(this.getClass()))
//                .shouldFailOnError(true)
//                .syncIterations(false)
//                .build();
//            new Runner(opt).run();
//        }
//    }
}
