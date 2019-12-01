# Project Jigsaw

References:

* [Project Jigsaw: Module System Quick-Start Guide](https://openjdk.java.net/projects/jigsaw/quick-start)
* [JEP 261: Module System](https://openjdk.java.net/jeps/261)

## Exploded

Compile and run with exploded folders:

```bash
make clean build run
```

Alternatively, you can call `build-exploded` instead of `build`,
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

## Reference jar in compilation

In this example, `astro` is first build and packaged into a jar.
Then, `greetings` is compiled against the `astro` jar.
Finally, the `greetings` jar is built and run.

```bash
make clean build-astro package-astro build-greetings package-greetings run-jar
```
