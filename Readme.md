# Java FFI benchmarks

Benchmarks for Java JNI (using JavaCPP) vs Project Panama with JDK (openjdk 19-panama 2022-09-20)

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

## Create Java bindings for `unistd.h` using jextract

**Linux**

```bash
export C_INCLUDE=/usr/include
jextract --source -d generated/src/main/java -t org.unix -I $C_INCLUDE $C_INCLUDE/unistd.h
```

**macOS**

```bash
export C_INCLUDE=/Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX.sdk/usr/include
jextract --source -d generated/src/main/java -t org.unix -I $C_INCLUDE $C_INCLUDE/unistd.h
```

## Build and run using Maven

When running the benchmark it by default only works for Linux. For macOS uncomment the appropriate function in `src/main/java/org/sample/FFIBenchmark.java`

```bash
mvn clean verify
java -jar target/benchmarks.jar
```

## Typical Results

These results vary because they were ran on a developer machine with other services running. Also, according
to the JMH docs, to avoid `blackholes` (methods that return void). e.g. If you call `getPid()` and return void the JVM will optimize by removing dead code. To ensure the code isn't removed the method returns a primitive (int).

Below are some results (smaller numbers are better).

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
