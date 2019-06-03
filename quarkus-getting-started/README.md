# Check server health

```bash
$ ./mvnw compile quarkus:dev
...
$ curl http://localhost:8080/management/health
{"outcome" : "success", "result" : "HEALTHY"}

$ curl http://localhost:8080/management/running
{"outcome" : "success", "result" : "running"}
```

# Getting started

```bash
$ ./mvnw compile quarkus:dev
...
$ curl http://localhost:8080/hello
hello, how are you?
```

# Native

```bash
$ ./mvnw package -Pnative -Dnative-image.docker-build=true
$ docker build -f src/main/docker/Dockerfile.native -t quarkus-quickstart/getting-started .
$ docker run -i --rm -p 8080:8080 quarkus-quickstart/getting-started
```
