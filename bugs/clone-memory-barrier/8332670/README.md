# Useful Commands

Run individual jcstress test:
```shell
$ TEST="copy.clone.arrays.small.plain.FloatTest" m test-jcstress
```

Run ArrayClone microbenchmark with C1 compilation only:
```shell
$ CONF=release TEST="micro:java.lang.ArrayClone" m micro-c1
```
