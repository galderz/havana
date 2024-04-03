# Usage

Setup JDK for this work:

```bash
$ cd jdk
$ git worktree add -b c2-opt-fp-min-max-same ../jdk-c2-opt-fp-min-max-same topic.0314.c2-opt-fp-min-max-same
```

Test newly added IR test:

```bash
$ m test TEST=test/hotspot/jtreg/compiler/intrinsics/math/TestFpMinMaxOpt.java
```

Test hotspot compiler:

```bash
$ m test-hotspot-compiler
```
