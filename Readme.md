# Java FFI benchmarks

Benchmarks for Java JNI vs Project Panama on Linux (Fedora 35) with JDK (openjdk 17-panama 2021-09-14)

## Run benchmarks

Clone the project

```bash
git clone 
```

Setup Java 17 panama build using SDK man

```bash
sdk install java 17.ea.3.pma-open
sdk use java 17.ea.3.pma-open
```

Create Java bindings for `unistd.h`

```bash
jextract --source -t src.main.java.org.unix -I /usr/include /usr/include/unistd.h
```

Build and run using Maven

```bash
mvn clean verify 
java -jar target/benchmarks.jar
```

