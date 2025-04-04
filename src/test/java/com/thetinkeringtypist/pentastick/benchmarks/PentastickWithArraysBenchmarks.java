package com.thetinkeringtypist.pentastick.benchmarks;

import com.thetinkeringtypist.pentastick.arrays.PentastickWithArrays;
import com.thetinkeringtypist.pentastick.arrays.Por;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(1)
@Warmup(iterations = 5)
@Measurement(iterations = 5)
public class PentastickWithArraysBenchmarks {
    PentastickWithArrays pentastickWithArrays;

    @Setup
    public void setup() {
        pentastickWithArrays = new PentastickWithArrays();
    }

    @Benchmark
    public PentastickWithArrays withListsRotateTLP() {
        pentastickWithArrays.rotate(Por.TLP);
        return pentastickWithArrays;
    }

    @Benchmark
    public PentastickWithArrays withListsRotateBLP() {
        pentastickWithArrays.rotate(Por.BLP);
        return pentastickWithArrays;
    }

    @Benchmark
    public PentastickWithArrays withListsRotateTL() {
        pentastickWithArrays.rotate(Por.TL);
        return pentastickWithArrays;
    }
    @Benchmark
    public PentastickWithArrays withListsRotateTF() {
        pentastickWithArrays.rotate(Por.TF);
        return pentastickWithArrays;
    }

    @Benchmark
    public PentastickWithArrays withListsRotateBF() {
        pentastickWithArrays.rotate(Por.BF);
        return pentastickWithArrays;
    }

    @Benchmark
    public PentastickWithArrays withListsRotateTR() {
        pentastickWithArrays.rotate(Por.TR);
        return pentastickWithArrays;
    }

    @Benchmark
    public PentastickWithArrays withListsRotateBR() {
        pentastickWithArrays.rotate(Por.BR);
        return pentastickWithArrays;
    }

    @Benchmark
    public PentastickWithArrays withListsRotateTRP() {
        pentastickWithArrays.rotate(Por.TRP);
        return pentastickWithArrays;
    }

    @Benchmark
    public PentastickWithArrays withListsRotateBRP() {
        pentastickWithArrays.rotate(Por.BRP);
        return pentastickWithArrays;
    }
}