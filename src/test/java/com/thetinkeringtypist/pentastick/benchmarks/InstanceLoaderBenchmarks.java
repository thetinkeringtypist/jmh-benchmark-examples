package com.thetinkeringtypist.pentastick.benchmarks;

import com.thetinkeringtypist.pentastick.instanceloader.InstanceLoader;
import com.thetinkeringtypist.pentastick.instanceloader.examples.Shape;
import com.thetinkeringtypist.pentastick.instanceloader.examples.Square;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Optional;
import java.util.concurrent.TimeUnit;


@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(1)
@Warmup(iterations = 5, time = 10, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 10, timeUnit = TimeUnit.SECONDS)
public class InstanceLoaderBenchmarks {
    Shape s;

    @Setup
    public void setup() {

    }

    @TearDown
    public void tearDown() {
        s = null;
    }

    /**
     * Benchmark how long it takes to create a red shape with a constructor.
     *
     * @param bh the blackhole that consumes objects to keep the compiler from removing dead code.
     */
    @Benchmark
    public void createSquareWithConstructor(Blackhole bh) {
        s = new Square("Red");
        bh.consume(s);
    }


    /**
     * Benchmark how long it takes to create a red shape with the InstanceLoader.
     *
     * @param bh the blackhole that consumes objects to keep the compiler from removing dead code.
     */
    @Benchmark
    public void createSquareWithInstanceLoader(Blackhole bh) {
        Optional<Shape> optional = InstanceLoader.load(Shape.class);
        if (optional.isEmpty()) {
            System.out.println("ERROR: square not on classpath");
        }

        s = optional.get();
        s.setColor("Red");
        bh.consume(s);
    }
}
