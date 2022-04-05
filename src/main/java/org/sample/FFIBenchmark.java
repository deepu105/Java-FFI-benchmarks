package org.sample;


import jdk.incubator.foreign.CLinker;
import jdk.incubator.foreign.FunctionDescriptor;
import jdk.incubator.foreign.NativeSymbol;
import jdk.incubator.foreign.ValueLayout;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.lang.invoke.MethodHandle;
import java.util.concurrent.TimeUnit;

@Fork(warmups = 2, value = 2, jvmArgs = {"-Xms512m", "-Xmx1024m", "--enable-native-access=ALL-UNNAMED", "--add-modules", "jdk.incubator.foreign"})
@BenchmarkMode(value = Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 500, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 20, time = 500, timeUnit = TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class FFIBenchmark {

    // get System linker
    private static final CLinker linker = CLinker.systemCLinker();
    private static final NativeSymbol nativeSymbol = linker.lookup("getpid").get();
    // predefine symbols and method handle info
    private static final MethodHandle getPidMH = linker.downcallHandle(
            nativeSymbol,
            FunctionDescriptor.of(ValueLayout.OfInt.JAVA_INT));

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(FFIBenchmark.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }

// uncommnet if running on macOS
//    @Benchmark
//    public int JNI() {
//        return org.bytedeco.javacpp.macosx.getpid();
//    }

    @Benchmark
    public int JNI() {
        return org.bytedeco.javacpp.linux.getpid();
    }

    @Benchmark
    public int panamaDowncall() throws Throwable {
        return (int) getPidMH.invokeExact();
    }

    @Benchmark
    public int panamaJExtract() {
        return org.unix.unistd_h.getpid();
    }
}
