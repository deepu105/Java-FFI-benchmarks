# Java FFI benchmarks

Benchmarks for Java JNI vs Project Panama on Linux (Fedora 35) with JDK (openjdk 17-panama 2021-09-14)

Setup instructions for benchmark:

## Clone the project

```bash
git clone https://github.com/deepu105/Java-FFI-benchmarks.git
```

## Setup Java 19 panama EA build using SDK man

```bash
sdk install java 19.ea.1.pma-open
sdk use java 19.ea.1.pma-open
```

## Create Java bindings for `unistd.h`

Linux

```bash
jextract --source -d generated/src/main/java -t org.unix -I /usr/include /usr/include/unistd.h
```

MacOS

```
export C_INCLUDE=/Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX.sdk/usr/include
jextract --source -d generated/src/main/java -t org.unix -I $C_INCLUDE $C_INCLUDE/unistd.h
```

## Build and run using Maven

When running the benchmark it currently only works for Linux. For macOS uncomment the appropriate function in `src/main/java/org/sample/FFIBenchmark.java`

```bash
mvn clean verify
java -jar target/benchmarks.jar
```

## Typical Results

These results vary because they were ran on a developer machine with other services running. Also, according
to the Java Microbenchmark Harness docs, to avoid `blackholes` (methods that return void). e.g. If you call getPid() and return void the JVM will optimize by removing dead code. To ensure the code isn't removed the method returns a primitive (int).

Below smaller numbers the better.

### macOS (JDK 17)

```text
Benchmark                    Mode  Cnt  Score   Error  Units
FFIBenchmark.JNI             avgt   40  9.698 ± 0.532  ns/op
FFIBenchmark.panamaDowncall  avgt   40  8.431 ± 0.096  ns/op
FFIBenchmark.panamaJExtract  avgt   40  8.488 ± 0.099  ns/op
```

### Linux (JDK 19)

```text
Benchmark                    Mode  Cnt   Score   Error  Units
FFIBenchmark.JNI             avgt   40  50.221 ± 0.512  ns/op
FFIBenchmark.panamaDowncall  avgt   40  49.382 ± 0.701  ns/op
FFIBenchmark.panamaJExtract  avgt   40  49.946 ± 0.721  ns/op
```
