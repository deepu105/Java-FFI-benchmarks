# Java FFI benchmarks

Benchmarks for Java JNI vs Project Panama on Linux (Fedora 35) with JDK (openjdk 17-panama 2021-09-14)

Setup instructions for benchmark:

## Clone the project

```bash
git clone 
```

## Setup Java 17 panama build using SDK man

```bash
sdk install java 17.ea.3.pma-open
sdk use java 17.ea.3.pma-open
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

When running the benchmark it currently only works for Linux.
```bash
mvn clean verify 
java -jar target/benchmarks.jar
```

## Typical Results

20 count

```console
Benchmark                    Mode  Cnt  Score   Error  Units
FFIBenchmark.JNI             avgt   20  9.103 ± 0.148  ns/op
FFIBenchmark.panamaDowncall  avgt   20  8.265 ± 0.114  ns/op
FFIBenchmark.panamaJExtract  avgt   20  8.528 ± 0.112  ns/op
```

40 count

```console
Benchmark                    Mode  Cnt  Score   Error  Units
FFIBenchmark.JNI             avgt   40  49.182 ± 1.079  ns/op
FFIBenchmark.panamaDowncall  avgt   40  50.746 ± 0.702  ns/op
FFIBenchmark.panamaJExtract  avgt   40  48.838 ± 1.461  ns/op
```
