package com.thetinkeringtypist.pentastick.benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(1)
@Warmup(iterations = 5)
@Measurement(iterations = 5)
public class StreamBenchmarks {

    @Param ("10000000")
    int n;
    List<Integer> ascending;
    List<Integer> descending;
    Random rand = new Random(77);

    @Setup
    public void setup() {
        ascending = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            ascending.add(rand.nextInt());
        }

        // Sort into worst-case evaluation order
        ascending.sort(Integer::compareTo);

        // Sort into worst-case evaluation order
        descending = ascending.reversed();
    }

    @Benchmark
    public void streamFindMax(Blackhole bh) {
        bh.consume(ascending.stream().max(Integer::compareTo).get());
    }

    @Benchmark
    public void streamFindMin(Blackhole bh) {
        bh.consume(ascending.stream().min(Integer::compareTo).get());
    }

    @Benchmark
    public void forLoopFindMax(Blackhole bh) {
        int size = ascending.size();
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < size; i++) {
            int value = ascending.get(i);
            if (value >= max) {
                max = value;
            }
        }

        bh.consume(max);
    }

    @Benchmark
    public void forLoopFindMin(Blackhole bh) {
        int size = descending.size();
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < size; i++) {
            int value = descending.get(i);
            if (value <= min) {
                min = value;
            }
        }

        bh.consume(min);
    }
}
