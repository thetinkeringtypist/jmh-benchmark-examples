package com.thetinkeringtypist.pentastick.benchmarks;

import com.thetinkeringtypist.pentastick.lists.PentastickWithLists;
import com.thetinkeringtypist.pentastick.lists.Por;
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
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(1)
@Warmup(iterations = 5)
@Measurement(iterations = 5)
public class PentastickWithListsBenchmarks {
    PentastickWithLists pentastickWithLists;

    @Setup
    public void setup() {
        pentastickWithLists = new PentastickWithLists();
    }

    @Benchmark
    public PentastickWithLists withListsRotateTLP() {
        pentastickWithLists.rotate(Por.TLP);
        return pentastickWithLists;
    }

    @Benchmark
    public PentastickWithLists withListsRotateBLP() {
        pentastickWithLists.rotate(Por.BLP);
        return pentastickWithLists;
    }

    @Benchmark
    public PentastickWithLists withListsRotateTL() {
        pentastickWithLists.rotate(Por.TL);
        return pentastickWithLists;
    }
    @Benchmark
    public PentastickWithLists withListsRotateTF() {
        pentastickWithLists.rotate(Por.TF);
        return pentastickWithLists;
    }

    @Benchmark
    public PentastickWithLists withListsRotateBF() {
        pentastickWithLists.rotate(Por.BF);
        return pentastickWithLists;
    }

    @Benchmark
    public PentastickWithLists withListsRotateTR() {
        pentastickWithLists.rotate(Por.TR);
        return pentastickWithLists;
    }

    @Benchmark
    public PentastickWithLists withListsRotateBR() {
        pentastickWithLists.rotate(Por.BR);
        return pentastickWithLists;
    }

    @Benchmark
    public PentastickWithLists withListsRotateTRP() {
        pentastickWithLists.rotate(Por.TRP);
        return pentastickWithLists;
    }

    @Benchmark
    public PentastickWithLists withListsRotateBRP() {
        pentastickWithLists.rotate(Por.BRP);
        return pentastickWithLists;
    }
}