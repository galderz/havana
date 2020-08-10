Run stress test with:

```
MAVEN_OPTS="-ea" mvn clean compile exec:java -Dexec.mainClass="lang.ClassValueStress"
```

To further stress it, run with:

```
MAVEN_OPTS="-ea -XX:+StressLCM, -XX:+StressGCM" mvn clean compile exec:java -Dexec.mainClass="lang.ClassValueStress"
```
