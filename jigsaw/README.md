# Project Jigsaw

References:

* [Project Jigsaw: Module System Quick-Start Guide](https://openjdk.java.net/projects/jigsaw/quick-start)
* [JEP 261: Module System](https://openjdk.java.net/jeps/261)

## Exploded

Compile and run with exploded folders:

```bash
make clean build run
```

Alternatively, you can call `build-separate` instead of `build`,
to compile each module separately.
It demonstrates how compilations are layered when depending on other modules.

## Packaging

This section explores how to create jars out of modules,
how to combine and run them.

```bash
make clean build package run-jar
```

Jar modules can be inspected to find out more about them:

```bash
make describe
```
