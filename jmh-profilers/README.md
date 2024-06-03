# JMH Profilers

Run a JMH benchmark with perfasm and get the diassembled assembly code.

Run a JMH benchmark with perf profiler and get branches and instructions:
```shell
PROF=perf:events=branches,instructions m prof
```

Run a JMH benchmark with perf norm profiler and get branches and instructions:
```shell
PROF=perfnorm:events=branches,instructions m prof
```
