package org.sample;

import jdk.incubator.foreign.CLinker;
import jdk.incubator.foreign.FunctionDescriptor;
import jdk.incubator.foreign.SymbolLookup;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.lang.invoke.MethodType;
import java.util.concurrent.TimeUnit;

@Fork(warmups = 2, value = 2, jvmArgs = {"-Xms2G", "-Xmx2G", "--enable-native-access=ALL-UNNAMED", "--add-modules", "jdk.incubator.foreign"})
@BenchmarkMode(value = Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 500, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 20, time = 500, timeUnit = TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class FFIBenchmark {

    // get System linker
    private final CLinker linker = CLinker.getInstance();
    private final SymbolLookup lookup = CLinker.systemLookup();

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(FFIBenchmark.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }

    @Benchmark
    public void JNI() {
        callJNI();
    }

    @Benchmark
    public void panamaDowncall() throws Throwable {
        callPanamaDowncall();
    }

    @Benchmark
    public void panamaJExtract() {
        callPanamaJExtract();
    }

    public int callJNI() {
        return org.bytedeco.javacpp.linux.getpid();
    }

    public long callPanamaDowncall() throws Throwable {
        var getpid = linker.downcallHandle(
                lookup.lookup("getpid").get(),
                MethodType.methodType(int.class),
                FunctionDescriptor.of(CLinker.C_INT));
        return ((int) getpid.invokeExact());
    }

    public long callPanamaJExtract() {
        return org.unix.unistd_h.getpid();
    }
}
