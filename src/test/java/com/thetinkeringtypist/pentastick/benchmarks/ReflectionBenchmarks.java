package com.thetinkeringtypist.pentastick.benchmarks;

import com.thetinkeringtypist.pentastick.instanceloader.examples.Shape;
import com.thetinkeringtypist.pentastick.instanceloader.examples.Square;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(1)
@Warmup(iterations = 5, time = 10, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 10, timeUnit = TimeUnit.SECONDS)
public class ReflectionBenchmarks {

    /**
     * This benchmark is likely optimized by the compiler because the instanceof call always evaluates to true.
     *
     * @param bh the blackhole that consumes objects to keep the compiler from removing dead code.
     */
    @Benchmark
    public void implicitInstanceOf(Blackhole bh) {
        Object s = new Square("Red");

        if (s instanceof Square) {
            s = new Square("Green");
        }

        bh.consume(s);
    }

    /**
     * This benchmark is likely optimized by the compiler because the isInstance call always evaluates to true.
     *
     * @param bh the blackhole that consumes objects to keep the compiler from removing dead code.
     */
    @Benchmark
    public void explicitInstanceOf(Blackhole bh) {
        Object s = new Square("Red");

        Class clazz = Shape.class;
        if (clazz.isInstance(s)) {
            s = new Square("Green");
        }

        bh.consume(s);
    }
}
