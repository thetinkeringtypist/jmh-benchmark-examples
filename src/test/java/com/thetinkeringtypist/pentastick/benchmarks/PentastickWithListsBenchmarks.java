package com.thetinkeringtypist.pentastick.benchmarks;

import com.thetinkeringtypist.pentastick.lists.Por;
import com.thetinkeringtypist.pentastick.lists.PentastickWithLists;
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
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(1)
@Warmup(iterations = 5, time = 5, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 5, timeUnit = TimeUnit.SECONDS)
public class PentastickWithListsBenchmarks {
    @State(Scope.Benchmark)
    public static class BenchmarkState {
        @Param({"TLP", "BLP", "TL", "BL", "TF", "BF", "TR", "BR", "TRP", "BRP"})
        Por por;

        PentastickWithLists pentastick;

        @Setup
        public void setup() {
            pentastick = new PentastickWithLists();
        }

        @TearDown
        public void tearDown() {
            pentastick = null;
        }
    }

    @Benchmark
    public PentastickWithLists rotate(BenchmarkState state, Blackhole blackhole) {
        state.pentastick.rotate(state.por);
        return state.pentastick;
    }
}