package com.thetinkeringtypist.pentastick.benchmarks;

import com.thetinkeringtypist.pentastick.arrays.PentastickUsingBytes;
import com.thetinkeringtypist.pentastick.arrays.PentastickUsingChars;
import com.thetinkeringtypist.pentastick.arrays.PentastickUsingDoubles;
import com.thetinkeringtypist.pentastick.arrays.PentastickUsingFloats;
import com.thetinkeringtypist.pentastick.arrays.PentastickUsingInts;
import com.thetinkeringtypist.pentastick.arrays.PentastickUsingLongs;
import com.thetinkeringtypist.pentastick.arrays.PentastickUsingShorts;
import com.thetinkeringtypist.pentastick.arrays.Por;
import com.thetinkeringtypist.pentastick.arrays.Puzzle;
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

@Fork(1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 5, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 5, timeUnit = TimeUnit.SECONDS)
public class PentastickDataTypeBenchmarks {
    @State(Scope.Benchmark)
    public static class BenchmarkState {
        @Param({"TLP", "BLP", "TL", "BL", "TF", "BF", "TR", "BR", "TRP", "BRP"})
        Por por;

        @Param({"byte[][]", "char[][]", "short[][]", "int[][]", "long[][]", "float[][]", "double[][]"})
        String dataType;

        public Puzzle puzzle;

        @Setup
        public void setup() {
            puzzle = createPuzzle(dataType);
        }

        @TearDown
        public void teardown() {
            puzzle = null;
        }

        public Puzzle createPuzzle(final String dataType) {
            return switch(dataType) {
                case "byte[][]" -> new PentastickUsingBytes();
                case "char[][]" -> new PentastickUsingChars();
                case "short[][]" -> new PentastickUsingShorts();
                case "int[][]" -> new PentastickUsingInts();
                case "long[][]" -> new PentastickUsingLongs();
                case "float[][]" -> new PentastickUsingFloats();
                case "double[][]" -> new PentastickUsingDoubles();
                default -> null;
            };
        }
    }

    @Benchmark
    public Puzzle rotate(final BenchmarkState state, final Blackhole bh) {
        Puzzle pentastick = state.puzzle;
        Por por = state.por;
        pentastick.rotate(por);

        return pentastick;
    }

    @Benchmark
    public Puzzle rotateUnrolled(final BenchmarkState state, final Blackhole bh) {
        Puzzle pentastick = state.puzzle;
        Por por = state.por;
        pentastick.rotateUnrolled(por);

        return pentastick;
    }
}
