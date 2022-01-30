package org.sample;

import jdk.incubator.foreign.*;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.lang.invoke.MethodHandle;
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
    private static final CLinker linker = CLinker.getInstance();
    private static final SymbolLookup lookup = CLinker.systemLookup();

    // predefine symbols and method handle info
    private static final MethodHandle getPidMH = linker.downcallHandle(
            lookup.lookup("getpid").get(),
            MethodType.methodType(int.class),
            FunctionDescriptor.of(CLinker.C_INT));;

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(FFIBenchmark.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }

//    @Benchmark
//    public void JNI() {
//        org.bytedeco.javacpp.macosx.getpid();
//    }

    @Benchmark
    public void JNI() {
        org.bytedeco.javacpp.linux.getpid();
    }

    @Benchmark
    public void panamaDowncall() throws Throwable {
        int pid = (int) getPidMH.invokeExact();
    }

    @Benchmark
    public void panamaJExtract() {
       org.unix.unistd_h.getpid();
    }
}
