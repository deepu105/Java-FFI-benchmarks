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

